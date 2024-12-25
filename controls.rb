control 'Verify Oracle Linux VM SL1 services' do
  impact 1.0
  title 'Verify the Oracle Linux VM environment'

  describe command('cat /etc/em7-release') do
    its('stdout') { should include 'EM7 10.1.0 [build 2166]' }
  end

  describe command('cat /opt/em7/nextui/node_modules/@sciencelogic/ap2/package.json | grep version') do
    its('stdout') { should include '"version": "5.222.1-release.25"' }
  end
  
  expected_version = '10.4.12'

  describe command('rpm -qa ^MariaDB-*') do
    its('stdout') {
      # Extract the versions from the output, allowing for any suffix (like -1.el7)
      maria_db_packages = subject.stdout.lines.map { |line| line.strip }
  
      # Verify that each package contains the expected version (ignoring the suffix after the version number)
      maria_db_packages.each do |pkg|
        expect(pkg).to match(/MariaDB-(server|shared|client|common|backup|compat)-#{Regexp.escape(expected_version)}.*\.el7\.centos\.x86_64/)
      end
    }
  end
  

  describe command('cat /opt/em7/nextui/node_modules/@sciencelogic/ap2/package.json | grep sl-em7-gql') do
    its('stdout') { should include '"@sciencelogic/sl-em7-gql": "55.39.2-release.25"' }
  end
end
