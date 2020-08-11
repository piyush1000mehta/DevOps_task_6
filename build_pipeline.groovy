buildPipelineView('DevOps_task_6') {
    filterBuildQueue()
    filterExecutors()
    title('DevOps_task_6 build Pipeline')
    displayedBuilds(5)
    selectedJob('seed_job')
    alwaysAllowManualTrigger()
    showPipelineParameters()
    refreshFrequency(60)
}
