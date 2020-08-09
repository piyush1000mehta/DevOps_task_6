job('job2_deploy6'){
description('this job will deploy code')
triggers {
        upstream('seed_job', 'SUCCESS')
    }

steps {
shell('''if ! sudo kubectl get pvc | grep php-deploy-pvc
         then
          sudo kubectl create -f /root/.jenkins/workspace/seed_job/php_deploy_pvc.yml
         fi

         if ls /root/.jenkins/workspace/seed_job/ | grep .php
         then
          if sudo kubectl get deploy | grep php-deploy
          then 
           PODS=$(sudo kubectl get pods -l app=php -o jsonpath="{.items[*].metadata.name}")
           for i in $PODS
            do 
             sudo kubectl cp /root/.jenkins/workspace/seed_job/*.php $i:/var/www/html/
            done
          else
           sudo kubectl  create -f /root/.jenkins/workspace/seed_job/php_deploy.yml
           sleep 25
           PODS=$(sudo kubectl get pods -l app=php -o jsonpath="{.items[*].metadata.name}")
           for i in $PODS
            do 
             sudo kubectl cp /root/.jenkins/workspace/seed_job/*.php $i:/var/www/html/
            done
          fi 
         fi  

         if ! sudo kubectl get svc | grep php-deploy-svc
         then
          sudo kubectl create -f /root/.jenkins/workspace/seed_job/php_deploy_svc.yml
         fi''') 
}
} 


job('job3_test6'){
description('this job is for testing' )
triggers {
        upstream('job2_deploy6', 'SUCCESS')
    }
steps{
shell('''nodeport=$(sudo kubectl get svc -o jsonpath={.items[*].spec.ports[*].nodePort})

         status=$(curl -o /dev/null -s -w "%{http_code}" http://192.168.99.100:$nodeport)

         if [[ $status == 200 ]]
         then
         echo " deployed code is working fine "
         exit 0
         else 
         exit 1
         fi''')
}
publishers {
        downstreamParameterized {
            trigger('job4_notify_email6') {
				condition('FAILED')
				triggerWithNoParameters()
            }
        }
    }
}


job(' job4_notify_email6'){
description(' this job will send email to developers ')
 publishers {
        extendedEmail {
            recipientList('piyushmehta9909@gmail.com')
            defaultSubject('Oops')
            defaultContent('code is not working.Check code')
            contentType('text/html')
            triggers {
              always(){
          sendTo{
            recipientList()
                }
            }
        }
    }
}
}
