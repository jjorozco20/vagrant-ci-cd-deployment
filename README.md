Once that you have this repo cloned on your local, these are the requirements to accomplish before you get started: 

 

Install Vagrant 

This is the page in where you can find the installation method for your OS 
https://developer.hashicorp.com/vagrant/docs/installation 

 

Install Virtualbox/VMWare (Vagrant provider, it uses this to build your VMs)  

VMWare: https://blogs.vmware.com/workstation/2024/05/vmware-workstation-pro-now-available-free-for-personal-use.html 

Virtualbox: https://www.virtualbox.org/wiki/Downloads 

 

Install Jenkins 

You can follow this page based on your OS 

https://www.jenkins.io/doc/book/installing/ 

 

Install Chef Inspec 

You can follow this page based on your OS 

https://docs.chef.io/inspec/install/ 

 

How to start with Vagrant? 

If you want to set it at your local environment (not deploying it using Jenkins pipeline): 

 

Review the Vagrantfile, it receives certain parameters to fulfil his task, so you need to have those values ready to pass them. 

 

 

The sl1 user and the password is the default one: em7admin for both 

 

Then, once that you have that, you are going to setup some vms to have a distributed system of the SL1 solution. This can be seen below in the code, we are using a box that has inside a clean install with the iso booting first instead of any OS that the vm can have inside of it.  

 

 

-------------------------------------------------------------------- 

If you don't have the box available, you can use this file to create a vm from scratch with the SL1 ISO file attached, so you can run the whole Vagrantfile with  

vagrant up  

and then it will create a fresh VM with the installation disk.  

Note: You need to have the ISO file that someone in the Team can provide you and you can use your local network for IPs. 

 

 

This is going to fail in the Vagrant's SSH connection method, this because we are booting from an ISO instead of an OS, that has the settings to let Vagrant do the remote entry, so cancel it at the shell, and if you want to create the other vm then run again 

 

vagrant up 

 

To follow the flow, my peer. 

 

Once that you have your VM(s), then it's time to create your vagrant box. To do so, follow these steps: 

 

Before you do something, let's talk. Once that you created your VM, there will be a SSH rule created for your NAT network pointing to your machine, so the box will contain this wicked rule, causing vagrant to crash because you can't define the same rule twice. So locate you VM ID using this: 

 

vboxmanage list vms 

 

And then it will show you something like this 

 

The first one is the name, the second one is the id that you need to retrieve first. Then run this command:  

 

VBoxManage modifyvm "[VM_ID]" --natpf1 delete "ssh" 

 

With that, we are all set. Follow the next steps.  

 

Locate the folder in which you are standing and then run 

 

vagrant package --base [VM_NAME] --output [BOX_FILE_NAME].box 

 

Now that you have the .box file, run this: 

 

vagrant box add [BOX_NAME] .\[BOX_FILE_NAME].box 

 

Now go to the step above to create your vms using your Local Box. 

-------------------------------------------------------------------- 

 

 

How to start with Chef Inspec? 

 

Go peep the Chef Inspec's documentation. 

 

 

-------------------------------------------------------------------- 

 

 

How to start with GitLab-CI? 

 

If you have the repo opened, you can see that we have a YAML file with the name .gitlab-ci.yml, this one is the pipeline that will ensure your code is built with quality and compliance. So the first step is to create it and then start scripting your whole integration process. 

 

This is an example on how to configure it. 

 

 

 

In our case, we have defined here that we want to run Rubocop and PSScriptAnalyzer lints first, then the Vagrant Validate command 

 

 

In our case, we are using GitLab Runners to create containers that are provided with libraries first, then run the commands that we declared in the script section. You can put certain rules to it in order to run in different environments or when pushes are done to some branches, so please go review the GitLab CI documentation. 

 

You can use a self-hosted runner (which is a server that you provision and then links with GitLab) to use more resources or images that are not available within GitLab Runners. 

 

To run a pipeline you need to commit changes or just go to your branch and select the pipelines at the left panel. 

 

 

 

And there you go, Now try to troubleshoot whole problems you will see in your console (if not, I'm so proud of you) until the pipeline runs smoothly. 

 

-------------------------------------------------------------------- 

 

How to start with Jenkins? 

 

You need to install Java SDK for it, so make sure you have it. 

 

To start Jenkins in a local use this 
 
``` 
sudo systemctl start jenkins  # Linux 

brew services start jenkins  # macOS 

java -jar jenkins.war         # Generic way 

 
``` 

 

In my case, I am using a docker container, more elegant. 

 

``` 

 

docker run --name jenkinscont -d ` 

    --network vagrant-jenkins-network ` 

    -p 8080:8080 -p 50000:50000 ` 

    -v jenkins_home:/var/jenkins_home ` 

    jenkins/jenkins:lts 

 

``` 

 

Then paste into the new task that you are going to create as a pipeline the jenkinsfile that is on the repo 

 

 

 

Scroll down and you will see the textarea 

 

 

 

 

Add parameters if you want to have certain prompt at runtime 

 

Search in the checkboxes the one marked up 
 

 

 

Remember to look at this in order to create your Jenkins agent, you will need to download certain stuff from it to launch the agent.java 



Then follow the steps and you are set with it. 