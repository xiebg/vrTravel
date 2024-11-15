# 导入必要的Python库
import os
from keras.preprocessing.image import ImageDataGenerator
from keras.models import Sequential
from keras.layers import Flatten, Dense, Dropout
from keras.optimizers import RMSprop
from keras.applications.vgg16 import VGG16
from keras.layers import BatchNormalization
import tensorflow as tf

# 设置参数
img_size = (224, 224)  # 图片输入尺寸
batch_size = 32  # 批处理大小
num_epochs = 10  # 迭代次数
train_dir = 'data_output/train'  # 训练集路径
val_dir = 'data_output/val'  # 验证集路径
test_dir = 'data_output/test'  # 测试集路径
output_model_file = 'model_vgg16.h5'  # 输出模型文件名


# 创建基于VGG16的卷积神经网络模型
def create_vgg16():
    vgg16 = VGG16(weights='imagenet', include_top=False, input_shape=(img_size[0], img_size[1], 3))
    # 冻结VGG16的前15层（直到block4_pool）
    for layer in vgg16.layers[:15]:
        layer.trainable = False
    model = Sequential()
    model.add(vgg16)
    # 添加全局平均池化层和输出层，并应用dropout和批量归一化技术
    model.add(Flatten())
    model.add(Dense(1024, activation='relu'))
    model.add(Dropout(0.5))  # Dropout层，丢弃率为0.5
    model.add(Dense(len(os.listdir(train_dir)), activation='softmax'))
    return model


if __name__ == '__main__':
    train_datagen = ImageDataGenerator(rescale=1. / 255)
    val_datagen = ImageDataGenerator(rescale=1. / 255)
    test_datagen = ImageDataGenerator(rescale=1. / 255)

    train_generator = train_datagen.flow_from_directory(
        train_dir,
        target_size=img_size,
        batch_size=batch_size,
        class_mode='categorical')

    val_generator = val_datagen.flow_from_directory(
        val_dir,
        target_size=img_size,
        batch_size=batch_size,
        class_mode='categorical')

    test_generator = test_datagen.flow_from_directory(
        test_dir,
        target_size=img_size,
        batch_size=batch_size,
        class_mode='categorical')

    model = create_vgg16()
    # 添加批量归一化层
    model.add(Dense(2048, input_shape=model.output_shape[1:], activation='relu'))
    model.add(BatchNormalization())
    model.add(Dropout(0.5))
    model.add(Dense(2048, activation='relu'))
    model.add(BatchNormalization())
    model.add(Dropout(0.5))
    model.add(Dense(train_generator.num_classes, activation='softmax'))

    optimizer = RMSprop(lr=1e-4)
    model.compile(loss='categorical_crossentropy', optimizer=optimizer, metrics=['accuracy'])
    model.fit_generator(train_generator,
                        steps_per_epoch=train_generator.samples // batch_size,
                        epochs=num_epochs,
                        validation_data=val_generator,
                        validation_steps=val_generator.samples // batch_size,
                        verbose=1)
    loss, accuracy = model.evaluate_generator(test_generator)
    print("test set loss:{}".format(loss))
    print("test set accuracy:{}".format(accuracy))
    model.save(output_model_file)
    # 创建TFLite模型转换器
    converter = tf.lite.TFLiteConverter.from_keras_model(model)
    # 将VGG16模型转换为TFLite格式
    tflite_model = converter.convert()
    # 将TFLite格式的模型保存到磁盘上
    with open('vgg16.tflite', 'wb') as f:
        f.write(tflite_model)