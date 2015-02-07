Vagrant.require_version ">= 1.6.0", "< 1.8.0"

Vagrant.configure("2") do |config|

  config.vm.box = "puppetlabs/ubuntu-14.04-64-puppet"

  config.vm.provider "virtualbox" do |v|
    v.memory = 2048
    v.cpus = 2
  end

  config.vm.provider "vmware_fusion" do |v|
    v.vmx["memsize"] = "2048"
    v.vmx["numvcpus"] = "2"
    v.gui = false
  end

  config.vm.provision :shell, path: "bootstrap.sh"

  config.vm.synced_folder ".", "/home/vagrant/src"

  config.vm.hostname = "flapjax"
  config.ssh.forward_x11 = true
  config.vm.network "forwarded_port", guest: 80, host: 8080

end
