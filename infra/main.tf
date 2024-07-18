provider "aws" {
  region     = "eu-central-1"
}
resource "aws_instance" "my_instance" {
  ami           = "ami-0346fd83e3383dcb4" 
  instance_type = "t2.micro"             
}