import face_recognition
import cv2
from socket import *
from select import select
import sys
import time
import os

HOST='127.0.0.1'
PORT=8077
BUFSIZE=1024

clientSocket = socket(AF_INET, SOCK_STREAM)

video_capture = cv2.VideoCapture(0)

file_list = os.listdir("C:\\Project\\images")
imageNames = [i for i in file_list if i.find('.png') != -1 or i.find('jpg') != -1]

names = []
for image in imageNames:
    names.append(image.split('.')[0])

image_s = []
for imageName in imageNames:
    image_s.append(face_recognition.load_image_file("C:\\Project\\images\\%s" % imageName))

face_encoding_s = []
for image in image_s:
    face_encoding_s.append(face_recognition.face_encodings(image)[0])

#obama_face_encoding = face_recognition.face_encodings(obama_image)[0]


known_face_encodings = face_encoding_s

known_face_names = names

# Initialize some variables
face_locations = []
face_encodings = []
face_names = []
clientSocket.connect((HOST,PORT))


counter = 0

while True:
    
    ret, frame = video_capture.read()
    
    rgb_frame = frame[:, :, ::-1]

    # Find all the faces and face encodings in the current frame of video
    face_locations = face_recognition.face_locations(rgb_frame)
    face_encodings = face_recognition.face_encodings(rgb_frame, face_locations)
        
    face_names = []
    for (top, right, bottom, left), face_encoding in zip(face_locations, face_encodings):

        # See if the face is a match for the known face(s)
        matches = face_recognition.compare_faces(known_face_encodings, face_encoding)
        name = "Unknown"

        # If a match was found in known_face_encodings, just use the first one.
        if True in matches:
            first_match_index = matches.index(True)
            name = known_face_names[first_match_index]
            counter = 0
            clientSocket.sendall(bytes("20\n", 'UTF-8'))


        # Draw a box around the face
        cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)

        # Draw a label with a name below the face
        cv2.rectangle(frame, (left, bottom - 35), (right, bottom), (0, 0, 255), cv2.FILLED)
        font = cv2.FONT_HERSHEY_DUPLEX
        cv2.putText(frame, name, (left + 6, bottom - 6), font, 1.0, (255, 255, 255), 1)

    else :
        print(counter)
        counter+=1
        if counter > 4:
            clientSocket.sendall(bytes("10\n", 'UTF-8'))


    
    # Display the resulting image
    cv2.imshow('Video', frame)

    # Hit 'q' on the keyboard to quit!
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
    
    time.sleep(1)


clientSocket.close()
# Release handle to the webcam
video_capture.release()
cv2.destroyAllWindows()
exit(0)
