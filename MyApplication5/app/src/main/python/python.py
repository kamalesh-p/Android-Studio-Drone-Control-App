import cv2
import pathlib
import numpy as np
import base64
from PIL import Image
import io

cascade_path = pathlib.Path(cv2.__file__).parent.absolute() / "data/haarcascade_eye.xml"
eye_cascade = cv2.CascadeClassifier(str(cascade_path))

def main(Pic, Title):
    decoded_data = base64.b64decode(Pic)
    np_data = np.fromstring(decoded_data, np.uint8)

    image = cv2.imdecode(np_data, cv2.IMREAD_UNCHANGED)
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    
    ''' CODE START '''
    
    eyes = eye_cascade.detectMultiScale(image, scaleFactor = 1.2, minNeighbors = 4)
    for (x,y,w,h) in eyes:
        cv2.rectangle(image,(x,y),(x+w,y+h),(0, 255, 0),5)
    
    ''' CODE END '''
    
    #return stri
    pil_im = Image.fromarray(image)
    buff = io.BytesIO()
    pil_im.save(buff, format="PNG")
    img_str = base64.b64encode(buff.getvalue())
    result = ""+str(img_str, "utf-8")
    return result
