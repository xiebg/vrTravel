import cv2
import os
import numpy as np
# 缩放图像大小
def resize_image(image, target_size):
    resized_image = cv2.resize(image, target_size)
    return resized_image

# 中心裁剪图像
def center_crop(image, crop_size):
    h, w, c = image.shape
    x = int((w - crop_size[0]) / 2)
    y = int((h - crop_size[1]) / 2)
    cropped_image = image[y:y+crop_size[1], x:x+crop_size[0], :]
    return cropped_image

## 读取图像，解决imread不能读取中文路径的问题
def cv_imread(filePath):
    # 核心就是下面这句，一般直接用这句就行，直接把图片转为mat数据
    cv_img=cv2.imdecode(np.fromfile(filePath,dtype=np.uint8),-1)
    # imdecode读取的是rgb，如果后续需要opencv处理的话，需要转换成bgr，转换后图片颜色会变化
    # cv_img=cv2.cvtColor(cv_img,cv2.COLOR_RGB2BGR)
    return cv_img

# 图像路径与输出路径
data_dir = 'data'  # 数据集路径
output_dir = 'data_output'  # 图像缩放后进行中心裁剪的输出路径

# 目标尺寸和裁剪尺寸（均为输入模型所需的图像大小）
target_size = (256, 256)  # 注意首先将图片放大
crop_size = (224, 224)

if not os.path.exists(output_dir):
    os.makedirs(output_dir)
for label in ['train', 'val', 'test']:
    src_path = os.path.join(data_dir, label)
    target_path = os.path.join(output_dir, label)
    if not os.path.exists(target_path):
        os.makedirs(target_path)
    for loc in os.listdir(src_path):
        src_loc_path = os.path.join(src_path, loc)
        target_loc_path = os.path.join(target_path, loc)
        if not os.path.exists(target_loc_path):
            os.makedirs(target_loc_path)
        i = 0
        for file_name in os.listdir(src_loc_path):
            image_path = os.path.join(src_loc_path, file_name).replace('\\','/')
            # current_path = os.path.abspath(__file__)[:-7]
            print(image_path)
            # img = cv2.imread(image_path)
            img = cv_imread(image_path)

            if img is None:
                continue
            img_resized = resize_image(img, target_size)
            img_cropped = center_crop(img_resized, crop_size)
            save_path = os.path.join(target_loc_path, str(i) + '.jpg')
            # cv2.imwrite(save_path, img_cropped)
            cv2.imencode('.jpg', img_cropped)[1].tofile(save_path)
            i += 1