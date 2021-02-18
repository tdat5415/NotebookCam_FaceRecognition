import subprocess


command = 'java Exam'
popen = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)

#stdout, _ = popen.communicate()
#print(stdout)


command = 'python useraddslow.py'
popen = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)

#stdout, _ = popen.communicate()
#print(stdout)


