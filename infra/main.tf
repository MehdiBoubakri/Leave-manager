provider "aws" {
  region     = "eu-central-1"
}

resource "aws_security_group" "allow_ssh_and_web" {
  name = "allow_ssh_and_web"

  ingress {
    from_port = 22
    to_port   = 22
    protocol = "tcp"
    cidr_blocks = ["3.120.181.40/29"] # Allow authorize port 22 for the EC2 Instance Connect service IP addresses in eu-central-1
  }

  ingress {
    from_port = 80
    to_port   = 80
    protocol = "tcp"
    cidr_blocks = ["93.219.63.21/32"] # Allow web traffic only from the specified IP
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"] # Allow outbound traffic
  }
}
resource "aws_instance" "my_instance" {
  ami           = "ami-0346fd83e3383dcb4" 
  instance_type = "t2.micro"
  security_groups = [aws_security_group.allow_ssh_and_web.name]  
   user_data = <<EOF
#!/bin/bash
yum update -y
yum install docker -y
systemctl start docker
systemctl enable docker
EOF
}       

