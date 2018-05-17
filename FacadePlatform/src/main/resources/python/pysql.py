# -*- coding:utf-8 -*-
import pymysql


def execute_sql(statement):
    # 建立和mysql的连接
    connection = pymysql.connect(host='107.170.204.204', port=3306, user='aiuser', passwd='aiuser', db='ai_platform', charset='utf8')
    current_cursor = connection.cursor()
    # 执行语句
    row = current_cursor.execute(statement)
    connection.commit()
    current_cursor.close()
    connection.close()
    return row


def save_result(args, is_killed, sample, result):
    dictionary = {
        "model_id": args.model_id,
        "user_id": args.user_id,
        "exam_id": args.exam_id,
        "samples": sample,
        "is_killed": is_killed,
        "result_location": result
    }
    insert_statement = parse_insert_statement(dictionary, "t_model_process")
    print(insert_statement)
    execute_sql(insert_statement)


def parse_insert_statement(dictionary, table_name):
    statement = "insert into " + table_name + " ( "
    keys = []
    values = []
    for key, value in dictionary.items():
        keys.append(key)
        values.append("'"+str(value)+"'")
        # values.append(value)
    statement = statement + ','.join(keys) + " ) values ( " + ','.join(values) + " )"
    return statement





