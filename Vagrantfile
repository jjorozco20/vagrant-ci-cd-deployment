sl1_pwd = ENV['VM_USER']
sl1_user = ENV['VM_PASSWORD']

require 'securerandom'

# Function to generate a random MAC address for VirtualBox (without colons)
def generate_mac_address
  mac_prefix = "080027" # This prefix is used by VirtualBox
  mac_suffix = 3.times.map { SecureRandom.hex(1) }.join.upcase
  mac_address = "#{mac_prefix}#{mac_suffix}"
  puts "Generated MAC Address: #{mac_address}" # Log the generated MAC address
  mac_address
end

# Retrieve parameters from environment variables
vm_name1 = ENV['VM1_NAME']
ip_address1 = ENV['VM1_IP']
installation_type1 = ENV['VM1_INSTALL_TYPE']
adapter_name1 = "Intel(R) Wireless-AC 9462"

vm_name2 = ENV['VM2_NAME']
ip_address2 = ENV['VM2_IP']
installation_type2 = ENV['VM2_INSTALL_TYPE']
adapter_name2 = "Intel(R) Wireless-AC 9462"

Vagrant.configure("2") do |config|

  # Define the first VM
  config.vm.define vm_name1 do |vm1|
    begin
      vm1.vm.box = installation_type1
      vm1.vm.hostname = vm_name1

      # Generate a MAC address and log it
      mac1 = generate_mac_address
      puts "Assigning MAC #{mac1} to VM1" # Debug logging for MAC address assignment

      # Public network (bridged) with explicit MAC address (no colons)
      vm1.vm.network "public_network", bridge: adapter_name1, mac: mac1

      # SSH configuration
      vm1.ssh.username = sl1_user
      vm1.ssh.password = sl1_pwd
      vm1.ssh.host = ip_address1
    rescue => e
      puts "Ignoring error during VM #{vm_name1} setup: #{e.message}"
    end
  end

  # Define the second VM
  config.vm.define vm_name2 do |vm2|
    begin
      vm2.vm.box = installation_type2
      vm2.vm.hostname = vm_name2

      # Generate a MAC address and log it
      mac2 = generate_mac_address
      puts "Assigning MAC #{mac2} to VM2" # Debug logging for MAC address assignment

      # Public network (bridged) with explicit MAC address (no colons)
      vm2.vm.network "public_network", bridge: adapter_name2, mac: mac2

      # SSH configuration
      vm2.ssh.username = sl1_user
      vm2.ssh.password = sl1_pwd
      vm2.ssh.host = ip_address2
    rescue => e
      puts "Ignoring error during VM #{vm_name2} setup: #{e.message}"
    end
  end

end
