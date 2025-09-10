# Check OS family and version
  control 'os-family' do
  impact 1.0
  title 'Verify the OS family and version'
  desc 'Ensure the OS is either Ubuntu or CentOS and the version matches expectations.'

  describe os.family do
    it { should be_in ['debian', 'redhat'] }
  end

  if os.debian?
    describe os.name do
      it { should eq 'ubuntu' }
    end

    describe os.release do
      it { should cmp >= '20.04' }
    end
  end

  if os.redhat?
    describe os.name do
      it { should eq 'centos' }
    end

    describe os.release do
      it { should cmp >= '8' }
    end
  end
end

# Verify critical packages
control 'critical-packages' do
  impact 1.0
  title 'Ensure critical packages are installed'
  desc 'Check the presence of essential packages for system functionality.'

  %w[coreutils bash systemd].each do |pkg|
    describe package(pkg) do
      it { should be_installed }
    end
  end
end

# Validate filesystem layout
control 'filesystem-layout' do
  impact 0.9
  title 'Verify filesystem structure'
  desc 'Check for the presence of key directories.'

  %w[/etc /bin /usr /var /tmp].each do |dir|
    describe file(dir) do
      it { should exist }
      it { should be_directory }
    end
  end
end

# Validate network configuration
control 'networking-tools' do
  impact 0.8
  title 'Verify networking tools are available'
  desc 'Ensure the system has basic networking tools installed.'

  describe command('ip') do
    it { should exist }
  end

  describe command('ifconfig') do
    it { should exist }
  end
end
