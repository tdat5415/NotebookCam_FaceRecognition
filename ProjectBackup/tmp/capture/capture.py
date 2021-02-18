import cv2
import sys
#import numpy as np

capture = cv2.VideoCapture(0)
capture.set(cv2.CAP_PROP_FRAME_WIDTH, 1280)
capture.set(cv2.CAP_PROP_FRAME_HEIGHT, 720)

str = "Press 'Enter'"
argv = sys.argv[1]
file_name = argv + ".png"
path_file = "C:/Test/" + argv + "/" + file_name


while True:
    ret, frame = capture.read()

    #텍스트 삽입
    cv2.putText(frame, str, (450, 100), cv2.FONT_ITALIC, 2, (255, 255, 255),3)
    #cv2.circle(frame, center, radian, color, thickness)
    #원 삽입
    cv2.circle(frame, (660, 375), 250, (255, 255, 255), 2)
    cv2.imshow("Capture", frame)

    #Enter 키입력 받으면 캡쳐
    #if cv2.waitKey(1) > 0: break
    if cv2.waitKey(1) & 0xFF == 13:
      cv2.imwrite(path_file, frame)
      break

capture.release()
cv2.destroyAllWindows()
