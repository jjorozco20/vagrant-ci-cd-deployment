## Once that you have this repo cloned on your local, these are the requirements to accomplish before you get started: 

### Install Vagrant 

This is the page in where you can find the installation method for your OS 
https://developer.hashicorp.com/vagrant/docs/installation 

### Install Virtualbox (Vagrant provider, it uses this to build your VMs)  

Virtualbox: https://www.virtualbox.org/wiki/Downloads 

### Install Jenkins 

Don't worry for this, we are going to use a docker container.

### Install Chef Inspec 

You can follow this page based on your OS 

https://docs.chef.io/inspec/install/ 

for ubuntu: 
curl -L https://omnitruck.chef.io/install.sh | sudo bash -s -- -P inspec

You will need to run it fisrt, in order to be able to register for their Free Tier license

https://www.chef.io/license-generation-free-trial

```

## How to start with Vagrant? 

If you want to set it at your local environment (not deploying it using Jenkins pipeline): 

Review the Vagrantfile, it receives certain parameters to fulfil his task, so you need to have those values ready to pass them. 

```
# I am on a linux Ubuntu partition within my windows SSD, so that's why this adapter name
adapter_name = ENV['ADAPTER_NAME'] || "wlp0s20f3"

# VM 1 Config
vm_name1 = ENV['VM1_NAME'] || "YOUR SERVERNAME"
ip_address1 = ENV['VM1_IP'] || "YOUR IP" # I am using 192.168.1.200
vagrant_box1 = ENV['VM1_BOX'] || "YOUR BOX" # I am using ubuntu/focal64
vm_memory1 = ENV['VM1_MEMORY'] || 1024
vm_cpus1 = ENV['VM1_CPUS'] || 1

# VM 2 Config
vm_name2 = ENV['VM2_NAME'] || "YOUR SERVERNAME"
ip_address2 = ENV['VM2_IP'] || "YOUR IP" # I am using 192.168.1.201
vagrant_box2 = ENV['VM2_BOX'] || "YOUR BOX" # I am using centos/8
vm_memory2 = ENV['VM2_MEMORY'] || 1024
vm_cpus2 = ENV['VM2_CPUS'] || 1
```

The user and the password is the default one: `vagrant` for both.

Then, once that you have that, you are going to setup 2 vms to have a Vagrant box. W are using two boxes that are retrieved from public Vagrant Gallery.  

Then run: 

`vagrant up`  


And then it will create a fresh VM pulling the installation box.  

With that, we are all set. Follow the next steps.  

---

## How to start with Chef Inspec? 

Go peep the Chef Inspec's documentation. 

---

## How to start with GitHub Actions? 

If you have the repo opened, you can see that we have a YAML file with the name `.github/workflows/vagrant-workflow.yaml`, this one is the pipeline that will ensure your code is built with quality and compliance. So the first step is to create it and then start scripting your whole integration process. 

In our case, we have defined here that we want to run `rubocop -x` and `vagrant validate`.

In our case, we are using GitHub hosted agents to create containers that are provided with libraries first, then run the commands that we declared in the script section. You can put certain rules to it in order to run in different environments or when pushes are done to some branches, so please go review the GitHub Actions' documentation. 

You can use a self-hosted runner (which is a server that you provision and then links with GitHub) to use more resources or images that are not available within GitHub Agents. In our case, Vagrant validate cannot be applied using GitHub agents, so you need to config yours. This will help you:

1. Go to Actions>Runners>New Runner>New self-hosted runner and then follow the guide. You can use the repo's folder to save there your agent's stuff.

To run a pipeline you need to commit changes or just go to your branch and select the pipelines at the Actions option and re-run jobs. 

And there you go, Now try to troubleshoot whole problems you will see in your console (if not, I'm so proud of you) until the pipeline runs smoothly. 

---

## How to start with Jenkins? 

In my case, I am using a docker container, more elegant. 

``` 
docker run --name jenkinscont -d \
    -v jenkins_home:/var/jenkins_home \
    -p 8080:8080 -p 50000:50000 \
    jenkins/jenkins:lts 
``` 

Log into it using http://localhost:8080

It will ask you for a token, and you will need to enter the container to obtain it. Do this:

`docker exec -it jenkinscont /bin/sh`

Once that you are inside:

`cat /var/jenkins_home/secrets/initialAdminPassword`

And there you go.

Skip the form with your data, use the Admin stuff.

To start the agent you need to install Java

```
sudo apt install -y default-jre
```

Then go to Jenkins nodes to register your local machine as a self-hosted agent too, this to run vagrant. Follow the Linux installation.

```
curl -sO http://localhost:8080/jnlpJars/agent.jar
java -jar agent.jar -url http://localhost:8080/ -secret [Jenkins is going to give you the info there] -name "jenkins-agent" -webSocket -workDir "/your-path/vagrant-ci-cd-deployment"

```

Now you can run the pipeline, create a new Object and then select pipeline, and go right to below, you are going to paste there the Jenkinsfile to run it at your local machine, remember that you need to run it using the agent label, so go check what you need to put at line 2 or change the value if you used another label.

Then, just click on build/build with parameters and there you go.
You are going to be prompted into vagrant destroy, you can proceed or abort, this to ensure you can show a lil' demo before deleting it.

Follow for more :D
