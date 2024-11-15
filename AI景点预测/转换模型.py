import tensorflow as tf
# 读取已经训练好的TensorFlow模型
model = tf.keras.models.load_model('model_vgg16.h5')
# 将 TensorFlow 模型转换为 TFLite 模型（量化和优化）
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()
# 将 TFLite 模型保存为文件
open("model_vgg16.tflite","wb",encoding='utf-8').write(tflite_model)


# 直接保存
# import tensorflow as tf
# from tensorflow.keras.applications.vgg16 import VGG16
# import os
# os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
# # 加载预训练的VGG16模型
# model = VGG16(weights='imagenet')
# # 创建TFLite模型转换器
# converter = tf.lite.TFLiteConverter.from_keras_model(model)
# # 将VGG16模型转换为TFLite格式
# tflite_model = converter.convert()
# # 将TFLite格式的模型保存到磁盘上
# with open('vgg16.tflite', 'wb') as f:
#     f.write(tflite_model)