import cv2
import pathlib

def main(Pic, Title):
    image = cv2.imread(str(Pic))

    cascade_path = pathlib.Path(cv2.__file__).parent.absolute() / "data/haarcascade_eye.xml"
    eye_cascade = cv2.CascadeClassifier(str(cascade_path))
    eyes = eye_cascade.detectMultiScale(image, scaleFactor = 1.2, minNeighbors = 4)


    for (x,y,w,h) in eyes:
        cv2.rectangle(image,(x,y),(x+w,y+h),(0, 255, 0),5)

    return image

'''
image = cv2.imread("beauty.png")
cascade_path = pathlib.Path(cv2.__file__).parent.absolute() / "data/haarcascade_eye.xml"
print(cascade_path)
eye_cascade = cv2.CascadeClassifier(str(cascade_path))
eyes = eye_cascade.detectMultiScale(image, scaleFactor = 1.2,
                                    minNeighbors = 4)


for (x,y,w,h) in eyes:
    cv2.rectangle(image,(x,y),(x+w,y+h),(0, 255, 0),5)

cv2.imshow("Eye Detected", image)
cv2.waitKey(0)
cv2.destroyAllWindows()
'''