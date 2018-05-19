from keras import Model
from keras.models import load_model
import numpy as np
from keras import backend as K
from PIL import Image
import json
import pysql



def getActivationLayers(model):
    intermediate_layer_model_1 = Model(inputs=model.input, outputs=model.get_layer("activation_1").output)
    intermediate_layer_model_2 = Model(inputs=model.input, outputs=model.get_layer("activation_2").output)
    intermediate_layer_model_3 = Model(inputs=model.input, outputs=model.get_layer("activation_3").output)
    return intermediate_layer_model_1, intermediate_layer_model_2, intermediate_layer_model_3

def processDetailModel(model_path, images_data):
    # 加载模型
    model = load_model(model_path)
    # 获取神经网络中三个隐藏层模型
    activation1, activation2, activition3 = getActivationLayers(model)

    images = images_data

    # 按层执行
    hidden_activation_1_output = activation1.predict(images)
    # 根据relu特性 非零的地方的神经元是激活的 所以将隐藏层1输出的神经元结果非零的位置置为1
    # hidden_activation_1_output[hidden_activation_1_output != 0] = 1
    result_level_1 = hidden_activation_1_output

    hidden_activation_2_output = activation2.predict(images)
    result_level_2 = hidden_activation_2_output

    hidden_activation_3_output = activition3.predict(images)
    result_level_3 = hidden_activation_3_output

    #执行结果
    predict = model.predict(images)
    # 统计结果
    result = []
    for i in range(images_data.shape[0]):
        temp = []
        # temp.append(result_level_1[i].astype(np.int32).tolist())
        # temp.append(result_level_2[i].astype(np.int32).tolist())
        # temp.append(result_level_3[i].astype(np.int32).tolist())
        temp.append(result_level_1[i].tolist())
        temp.append(result_level_2[i].tolist())
        temp.append(result_level_3[i].tolist())
        result.append(temp)
    K.clear_session()

    return predict, result


class ModelClass:
    # 模型描述，可选
    description = "class introduction"

    #初始化类
    def __init__(self):
        self.description = "MNIST 手写数字模型"

    # 执行模型的方法，方法名不能修改
    def processModel(self, args):
        # 处理输入样本
        imagePathList = args.sample_list
        imagePathList = imagePathList[1:-1]
        imageArray = imagePathList.split(",")

        images_data = []
        number = len(imageArray)
        for i in range(number):
            im = Image.open(imageArray[0])
            out = im.resize((28, 28), Image.ANTIALIAS)
            im_arr = np.array(out.convert('L'))
            data = im_arr.flatten()
            images_data.append(data)
        images_data = np.array(images_data)

        # 执行正确模型
        result_standard, active_data_standard = processDetailModel(args.standard_module_location, images_data)
        # 执行变异模型
        result_variation, active_data_variation = processDetailModel(args.module_location, images_data)


        # 比较杀死结果,解析出预测值
        is_kill = 0
        result_standard_values = []
        result_variation_values = []
        for i in range(number):
            result_standard_value = np.argmax(result_standard[i])
            result_variation_value = np.argmax(result_variation[i])
            result_standard_values.append(result_standard_value)
            result_variation_values.append(result_variation_value)
            if result_standard_value != result_variation_value:
                is_kill = 1
        # 处理有效数据，组织成json格式

        architecture = {"level": 3, "detail": [128, 64, 10]}
        activation = []
        for j in range(number):
            temp = {"result": int(result_standard_values[j]), "predict_result": int(result_variation_values[j]),
                    "path": imageArray[j], "standard_list": active_data_standard[j],
                    "activation_list": active_data_variation[j]}
            activation.append(temp)
        result = {"architecture": architecture, "activation": activation}
        result = json.dumps(result)

        # 存储数据库
        pysql.save_result(args, is_kill, imagePathList, result)






















