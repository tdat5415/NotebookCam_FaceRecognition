import os




file_list = os.listdir("images")
print(file_list)

images = [i for i in file_list if i.find('.png') != -1 or i.find('jpg') != -1]
print(images)

names = []
for image in images:
    names.append(image.split('.')[0])
print(names)
