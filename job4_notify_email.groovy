


job('job4_notify_email'){
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
buildPipelineView('DevOps_task_6'){
filterBuildQueue()
filterExecutors()
title('Web-Server Deployment from Groovy Script')
displayedBuilds(1)
selectedJob('seed_job')
alwaysAllowManualTrigger()
showPipelineParameters()
refreshFrequency(60)
}

}
