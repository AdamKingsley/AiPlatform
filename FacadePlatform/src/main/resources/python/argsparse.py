import argparse

# 参数配置
parser = argparse.ArgumentParser(description='执行模型脚本')
parser.add_argument('--user_id', type=str, help='用户ID')
parser.add_argument('--exam_id', type=str, help='考试ID')
parser.add_argument('--module_location', type=str, help='变异模型路径')
parser.add_argument('--standard_module_location', type=str, help='标准模型路径')
parser.add_argument('--script_file', type=str, help='脚本路径')
parser.add_argument('--sample_list', type=str, help='样本集合')
args = parser.parse_args()

# 动态加载脚本方法
script_module = args.script_file
script_class = 'ModelClass'

# 导入指定模块，导入时会执行全局方法。
ip_module = __import__(script_module)
# 使用getattr()获取imp_module的类
ip_class = getattr(ip_module, script_class)

# 动态加载类test_class生成类对象
class_obj = ip_class()
for attr in dir(class_obj):
    # 加载非__前缀的属性
    if attr[0] != '_':
        #  获取导入obj方法。
        process_model = getattr(class_obj, attr)
        # print(getattr(process_model, '__name__'))

        # 判断类属性是否为函数
        if hasattr(process_model, '__call__'):
            # 执行函数
            process_model(args)
