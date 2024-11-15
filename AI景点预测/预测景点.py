import os
import numpy as np
from keras.models import load_model
from keras.preprocessing import image

# 图像路径
image_path = 'test.jpg'

# 模型路径
model_path = 'model_vgg16.h5'

# 加载模型
model = load_model(model_path)

# 加载图片并将其预处理为模型输入的格式
img = image.load_img(image_path, target_size=(224, 224))
x = image.img_to_array(img)
x = np.expand_dims(x, axis=0)
x /= 255.0

# 使用模型进行预测, 最终结果为每个类别的概率值
probs = model.predict(x)

# 将概率转换为标签
labels = os.listdir('data/train')
for i in range(len(labels)):
    print('{} : {:.2f}%'.format(labels[i], probs[0][i] * 100.0))

# 预测出最高概率对应的标签名
predicted_label = labels[np.argmax(probs)]
print('Predicted Label:', predicted_label)