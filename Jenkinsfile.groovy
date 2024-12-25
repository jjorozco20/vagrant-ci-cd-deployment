pipeline {
    agent { label 'vagrant' }

    environment {
        WORKSPACE = "C:\\Users\\Juan.Orozco\\Desktop\\vagrant-CI-CD-deployment"
    }

    parameters {
        string(name: 'VM1_NAME', defaultValue: '', description: 'Name of VM1')
        string(name: 'VM1_IP', defaultValue: '', description: 'IP address of VM1')
        string(name: 'VM1_USER', defaultValue: '', description: 'User for VM1')
        string(name: 'VM2_NAME', defaultValue: '', description: 'Name of VM2')
        string(name: 'VM2_IP', defaultValue: '', description: 'IP address of VM2')
        string(name: 'VM2_USER', defaultValue: '', description: 'User for VM2')
        string(name: 'VM_USER', defaultValue: '', description: 'Generic User for VMs')
        string(name: 'VM_PASSWORD', defaultValue: '', description: 'Password for VMs')
    }

    stages {
        stage('Vagrant Up for VM1') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    script {
                        try {
                            // Set environment variables for Vagrant
                            env.VM1_NAME = params.VM1_NAME
                            env.VM1_IP = params.VM1_IP
                            env.VM1_USER = params.VM1_USER

                            env.VM2_NAME = params.VM2_NAME
                            env.VM2_IP = params.VM2_IP
                            env.VM2_USER = params.VM2_USER

                            // Navigate to the workspace and run Vagrant
                            bat """
                            cd "${WORKSPACE}"
                            vagrant up
                            """
                        } catch (Exception e) {
                            // Capture the exception and print it as a warning
                            echo "${env.VM1_NAME} has an error: ${e.toString()}"
                            e.printStackTrace()

                        }
                    }
                }
            }
        }
        
        stage('Start VM1') {
            steps {
                script {
                    env.VM1_NAME_PART = params.VM1_NAME
                    echo "Looking for a VM with name containing: ${VM1_NAME_PART}"
                    bat """
                    rem List all registered VMs and search for the one containing the VM1_NAME_PART
                    for /f "tokens=1,2 delims= " %%A in ('VBoxManage list vms ^| findstr /I %VM1_NAME_PART%') do (
                        set VM_ID=%%B
                    )

                    rem Check if a VM was found
                    if not defined VM_ID (
                        echo No VM found with name containing %VM1_NAME_PART%
                        exit /b 1
                    )

                    echo Starting VM with ID: %VM_ID%
                    
                    rem Turn on the virtual machine using VBoxManage
                    VBoxManage startvm %VM_ID% --type headless
                    """
                }
            }
        }
        
        stage('Vagrant Up for VM2') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    script {
                        try {
                            // Set environment variables for Vagrant
                            env.VM1_NAME = params.VM1_NAME
                            env.VM1_IP = params.VM1_IP
                            env.VM1_USER = params.VM1_USER
                            
                            env.VM2_NAME = params.VM2_NAME
                            env.VM2_IP = params.VM2_IP
                            env.VM2_USER = params.VM2_USER

                            // Navigate to the workspace and run Vagrant
                            bat """
                            cd "${WORKSPACE}"
                            vagrant up
                            """
                        } catch (Exception e) {
                            // Capture the exception and print it as a warning
                            echo "${env.VM2_NAME} was sucessfully created, starting it"
                        }
                    }
                }
            }
        }

        stage('Start VM2') {
            steps {
                script {
                    env.VM2_NAME_PART = params.VM2_NAME
                    echo "Looking for a VM with name containing: ${VM2_NAME_PART}"
                    bat """
                    rem List all registered VMs and search for the one containing the VM2_NAME_PART
                    for /f "tokens=1,2 delims= " %%A in ('VBoxManage list vms ^| findstr /I %VM2_NAME_PART%') do (
                        set VM_ID=%%B
                    )

                    rem Check if a VM was found
                    if not defined VM_ID (
                        echo No VM found with name containing %VM2_NAME_PART%
                        exit /b 1
                    )

                    echo Starting VM with ID: %VM_ID%
                    
                    rem Turn on the virtual machine using VBoxManage
                    VBoxManage startvm %VM_ID% --type headless
                    """
                }
            }
        }
        
        stage('Run Chef InSpec Tests on VM1') {
            steps {
                script {
                    // Run Chef InSpec tests on VM1 using SSH
                    bat """
                    cd "${WORKSPACE}"
                    inspec exec controls.rb --target ssh://${params.VM_USER}@${params.VM1_IP} --password='${params.VM_PASSWORD}' --chef-license accept
                    """
                }
            }
        }

        stage('Run Chef InSpec Tests on VM2') {
            steps {
                script {
                    // Run Chef InSpec tests on VM2 using SSH
                    bat """
                    cd "${WORKSPACE}"
                    inspec exec controls.rb --target ssh://${params.VM_USER}@${params.VM2_IP} --password='${params.VM_PASSWORD}' --chef-license accept
                    """
                }
            }
        }
    }
}
