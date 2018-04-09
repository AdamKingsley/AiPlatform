import xlrd
import os

def writePO(sheet) :
    # table = data.sheets()[0] #通过索引顺序获取
    # table = data.sheet_by_index(0) #通过索引顺序获取
    tableName = sheet.name
    print("write po class for table " + sheet.name)
    className = sheet.row(1)[1].value
    print("the class name is : " + className)
    po = "package cn.com.tiza.component.elec.entity;\n\n"

    po += "import java.util.Date;\n"
    po += "import java.math.BigDecimal;\n"
    po += "\n"

    po += "import org.beetl.sql.core.annotatoin.Table;\n\n"
    po += "import lombok.Getter;\n"
    po += "import lombok.Setter;\n"
    po += "import lombok.EqualsAndHashCode;\n\n"
    
    po += "import cn.com.tiza.beetl.sql.experimental.db.Logic;\n"
    po += "import cn.com.tiza.component.entity.BaseDateEntity;\n\n"

    po += "@Getter\n"
    po += "@Setter\n"

    po += "@Table(name = \"" + tableName + "\")\n"
    po += "@Logic(filed = \"delete_flag\")\n"
    po += "public class " + className + " extends BaseDateEntity {\n\n"

    po += "    private static final long serialVersionUID = 1L;\n\n"
    
    nrows = sheet.nrows
    for i in range(8, nrows) :
        colDbType = sheet.row(i)[1].value
        colType = "String"
        if "DECIMAL" == colDbType :
            colType = "BigDecimal"
        elif "BIGINT" == colDbType :
            colDbType = "BIGINT"
            colType = "Long"
        elif "INT" == colDbType :
            colDbType = "INTEGER"
            colType = "Long"
        elif "TINYINT" == colDbType :
            colType = "int"
        elif "DATETIME" == colDbType :
            colDbType = "DATE"
            colType = "Date"
        elif "DATE" == colDbType :
            colType = "Date"
        # print(sheet.row(i)[0].value)
        isPrimaryKey = "false"
        if 7 == i :
            isPrimaryKey = "true"
        desc = sheet.row(i)[5].value
        fieldName = sheet.row(i)[9].value
        if fieldName == "deleteFlag" :
            break
        po += "    /** \n"
        po += "     * " + desc + "\n"
        po += "    */ \n"

        po += "    private " + colType + " " + sheet.row(i)[9].value + ";\n\n"

    po += "}\n"
    # print(po)
    writeFile(po, "java", className + ".java")

def writeFile(str, folder, fileName) :
    if os.path.exists(folder) :
        folder += "/"
    else :
        idx = folder.find("/")
        print(idx)
        if idx > 0 :
            parent = folder[0: idx]
            if os.path.exists(parent) == False :
                os.mkdir(parent)
        os.mkdir(folder)
        folder += "/"
    fileName = folder + fileName

    if os.path.exists(fileName) :
        os.remove(fileName)

    fw = open(fileName,"w", encoding="utf-8") #打开要写入的文件
    fw.write(str) #写入文件
    fw.close()

def writeJava(data) :
    # for t in data.sheets() :
    #     print(t.name) 
    for i in range(3, len(data.sheets())) :
        # print(data.sheets()[i].name)
        sheet = data.sheets()[i]
        writePO(sheet)

def writeSql(data) :
    # table = data.sheets()[0] #通过索引顺序获取
    # table = data.sheet_by_index(0) #通过索引顺序获取
    sql = "/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;\n" \
        "/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;\n" \
        "/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;\n" \
        "/*!40101 SET NAMES utf8 */;\n" \
        "/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;\n" \
        "/*!40103 SET TIME_ZONE='+00:00' */;\n" \
        "/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;\n" \
        "/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;\n" \
        "/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;\n" \
        "/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;\n"

    for i in range(3, len(data.sheets())) :
        # print(data.sheets()[i].name)
        sheet = data.sheets()[i]
        tableName = sheet.name
        sql += "DROP TABLE IF EXISTS " + tableName + ";\n"
    sql += "\n"
    for i in range(3, len(data.sheets())) :
        # print(data.sheets()[i].name)
        sheet = data.sheets()[i]
        sql += createSql(sheet)
        # break
    for i in range(3, len(data.sheets())) :
        # print(data.sheets()[i].name)
        sheet = data.sheets()[i]
        sql += findFKAndIDX(sheet)
        # sql += createSql(sheet)

    sql += "/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;\n" \
        "/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;\n" \
        "/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;\n" \
        "/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;\n" \
        "/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;\n" \
        "/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;\n" \
        "/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;\n" \
        "/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */; \n" 
    
    writeFile(sql, "sql", "mysql.sql")

def createSql(sheet) :
    tableName = sheet.name
    tableDesc = sheet.row(0)[4].value
    print("write sql for table " + sheet.name)
    sql = "CREATE TABLE " + tableName + "(\n"
    nrows = sheet.nrows
    key = ""
    for i in range(7, nrows) :
        colDbType = sheet.row(i)[1].value
        desc = sheet.row(i)[5].value
        isPrimaryKey = "PK" == sheet.row(i)[6].value
        isAutoInc = "Y" == sheet.row(i)[7].value
        vlen = sheet.row(i)[2].value
        column = sheet.row(i)[0].value
        isNull = sheet.row(i)[3].value
        defaultVal = sheet.row(i)[4].value
        if isPrimaryKey and 7 != i :
            key += ","
        if isPrimaryKey :
            key += column
        # print(vlen)
        # print(column)
        if "INT" == colDbType :
            colDbType = "INT"
            sql += "    " + column + " " + colDbType + "(" + str(int(vlen)) + ") "
        elif "BIT" == colDbType :
            colDbType = "BIT"
            sql += "    " + column + " " + colDbType + "(" + str(int(vlen)) + ") "
        elif "BIGINT" == colDbType :
            colDbType = "BIGINT"
            sql += "    " + column + " " + colDbType + "(" + str(int(vlen)) + ") "
        elif "DECIMAL" == colDbType :
            sql += "    " + column + " " + colDbType + "(" + vlen + ")"
        elif "VARCHAR" == colDbType :
            sql += "    " + column + " " + colDbType + "(" + str(int(vlen)) + ") "
        elif "DATE" == colDbType :
            sql += "    " + column + " " + colDbType
        elif "DATETIME" == colDbType :
            sql += "    " + column + " " + colDbType
        elif "TIMESTAMP" == colDbType :
            sql += "    " + column + " " + colDbType
        elif "TINYINT" == colDbType :
            sql += "    " + column + " " + colDbType
        elif "LONGTEXT" == colDbType :
            sql += "    " + column + " " + colDbType
        canNull = True
        if isNull and isNull == "N" :
            canNull = False
        if canNull :
            sql += " DEFAULT NULL "
        else :
            sql += " NOT NULL "
        if isPrimaryKey :
            sql += " AUTO_INCREMENT "
        if isPrimaryKey and isAutoInc :
            sql += " AUTO_INCREMENT "
            
        sql += " COMMENT  '" + desc + "',\n"
    sql += "    PRIMARY KEY (" + key + ")\n"
    sql += ")  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='" + tableDesc + "';\n\n"
    print(sql)
    return sql

def findFKAndIDX(sheet) :
    tableName = sheet.name
    tableDesc = sheet.row(0)[4].value
    print("生成 索引和外键 for table " + sheet.name)
    sql = ""
    nrows = sheet.nrows
    for i in range(7, nrows) :
        colName = sheet.row(i)[0].value
        _col = sheet.row(i)[6].value
        _refTableName = sheet.row(i)[8].value
        if "FK" == _col : 
            sql += "ALTER TABLE `" + tableName + "` ADD CONSTRAINT `FK_" + tableName + "_" + str(i) + "` "
            sql += "FOREIGN KEY (`" + colName + "`) REFERENCES `" + _refTableName + "` (`ID`)  ON DELETE NO ACTION ON UPDATE NO ACTION;\n"
            # ALTER TABLE `T_PUMP_STATION_AID` ADD CONSTRAINT `FK_AID_PUMP_STATION_0` FOREIGN KEY (`PUMP_STATION_ID`) REFERENCES `T_PUMP_STATION` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
        elif "INDEX" == _col :
            sql += "ALTER TABLE `" + tableName + "` ADD INDEX `IDX_" + tableName + "_" + str(i) + "` (`" + colName + "` ASC);\n"
            # ALTER TABLE `t_pump_station` ADD INDEX `IDX_PUMP_STATION_1` (`NAME` ASC);
    # print(sql)
    if "" != sql :
        sql += "\n"
    return sql
# 将数据表生成PO,VO,Biz,Dao,Controller
f = '配电房数据表.xlsx'
data = xlrd.open_workbook(f)

writeJava(data)
writeSql(data)

def insertCategoryDataSql(sheet) :
    tableName = sheet.name
    print(tableName)
    print("write insert sql for T_PUB_CATEGORY")
    sql = "INSERT INTO `T_PUB_CATEGORY`(`CODE`,`NAME`,`DELETE_FLAG`)\n"
    sql += " VALUES "
    nrows = sheet.nrows
    key = ""
    # _id = 10000
    for i in range(1, nrows) :
        _cide = sheet.row(i)[0].value
        _name = sheet.row(i)[1].value
        sql += "('" + _cide + "','" + _name + "', 0),\n"
        
    return sql[0:len(sql) - 2] + ";\n"

def insertSortDataSql(sheet) :
    tableName = sheet.name
    print(tableName)
    print("write insert sql for T_PUB_SORT")
    sql = "INSERT INTO `T_PUB_SORT`(CATEGORY_ID, `CODE`,`NAME`, RESERVE1, DESCRIPTION, `DELETE_FLAG`)\n"
    sql += " VALUES "
    nrows = sheet.nrows
    key = ""
    # _id = 10000
    for i in range(1, nrows) :
        _category = sheet.row(i)[0].value
        _code = sheet.row(i)[1].value
        _name = sheet.row(i)[2].value
        _reserve1 = sheet.row(i)[3].value
        if _reserve1 :
            _reserve1 = str(int(_reserve1))
        _desc = sheet.row(i)[4].value
        
        sql += "(" + "(select id from T_PUB_CATEGORY where code = '" + _category + "')," + " '" + _code + "','" + _name + "', '" + _reserve1 +  "', '" + _desc + "', 0),\n"
        
    return sql[0:len(sql) - 2] + ";\n"

def insertUserSql(sheet) :
    tableName = sheet.name
    print("write insert sql for " + tableName)
    sql = "INSERT INTO `t_user`(`LOGIN_NAME`,`NAME`, PASSWORD, SALT, `ENABLED`, ORG_ID, ROLE_ID, `DELETE_FLAG`)\n"
    sql += " VALUES "
    nrows = sheet.nrows
    key = ""
    for i in range(1, nrows) :
        _name = sheet.row(i)[0].value
        _org = sheet.row(i)[1].value
        _role = sheet.row(i)[2].value
        sql += "('" + _name + "', '" + _name + "', 'EEEC178AA771E4DC913CB4D72B68E837435B2CCD', 'DE56F0259A57992F', 1,"
        sql += "(select id from t_org_unit where name = '" + _org + "')"
        sql += "(select id from t_role where code = '" + _role + "')"
        sql += "0),\n"
        
    return sql[0:len(sql) - 2] + ";\n"

def insertRoleSql(sheet) :
    tableName = sheet.name
    print("write insert sql for " + tableName)
    sql = "INSERT INTO `t_role`(`CODE`,`NAME`, `DELETE_FLAG`)\n"
    sql += " VALUES "
    nrows = sheet.nrows
    key = ""
    for i in range(1, nrows) :
        _code = sheet.row(i)[0].value
        _name = sheet.row(i)[1].value
        sql += "('" + _code + "', '" + _name + "', 0),\n"
        
    return sql[0:len(sql) - 2] + ";\n"


def insertRegionInfoSql(sheet) :
    tableName = sheet.name
    print("write insert sql for " + tableName)
    sql = "INSERT INTO `t_region_info`(ID, `CODE`,`NAME`, PARENT_ID, LEVEL)\n"
    sql += " VALUES "
    nrows = sheet.nrows
    key = ""
    for i in range(1, nrows) :
        _id = str(int(sheet.row(i)[0].value))
        _name = sheet.row(i)[1].value
        _level = str(int(sheet.row(i)[2].value))
        _parent_id = str(int(sheet.row(i)[3].value))
        _code = str(int(sheet.row(i)[4].value))
        sql += "(" + _id + ",'" + _code + "', '" + _name + "', " + _parent_id + ", " + _level + "),\n"
        
    return sql[0:len(sql) - 2] + ";\n"

def insertOrgUnitSql(sheet) :
    tableName = sheet.name
    print("write insert sql for " + tableName)
    sql = "INSERT INTO `t_org_unit`(`NAME`,DELETE_FLAG)\n"
    sql += " VALUES "
    nrows = sheet.nrows
    key = ""
    for i in range(1, nrows) :
        _name = sheet.row(i)[0].value
        sql += "('" + _name + "', 0),\n"
        
    return sql[0:len(sql) - 2] + ";\n"

def insertAttrSql(sheet) :
    tableName = sheet.name
    print("write insert sql for " + tableName)
    sql = "INSERT INTO `t_pub_attr`(CODE, `DESCRIPTION`, SORT_ID, UNIT_ID, DELETE_FLAG)\n"
    sql += " VALUES "
    nrows = sheet.nrows
    key = ""
    for i in range(1, nrows) :
        _code = sheet.row(i)[0].value
        _desc = sheet.row(i)[1].value
        _sort = sheet.row(i)[2].value
        _unit = sheet.row(i)[3].value
        sql += "('" + _code + "', '" + _desc + "', " + "(select id from T_PUB_SORT where name = '" + _sort + "'),"
        if _unit : 
            sql += "(select id from T_PUB_SORT where name = '" + _unit + "')"
        else :  
            sql += "null"
        sql += ", 0),\n"
    return sql[0:len(sql) - 2] + ";\n"


def writeInitData(data) :
    # table = data.sheets()[0] #通过索引顺序获取
    # table = data.sheet_by_index(0) #通过索引顺序获取
    sql = insertCategoryDataSql(data.sheets()[0])
    sql += insertSortDataSql(data.sheets()[1])

    sql += insertOrgUnitSql(data.sheets()[7])
    sql += insertRoleSql(data.sheets()[5])
    sql += insertUserSql(data.sheets()[4])
    sql += insertRegionInfoSql(data.sheets()[6])
    sql += insertAttrSql(data.sheets()[8])
        # break
    writeFile(sql, "sql", "init.sql")

data = xlrd.open_workbook("配电房基础数据.xlsx")
writeInitData(data)