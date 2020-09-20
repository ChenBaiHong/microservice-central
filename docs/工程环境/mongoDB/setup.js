// 创建文件中心数据库
db = db.getSiblingDB('file-center');
// 创建一个用户，设置密码和授予数据所有权限
db.createUser(
    {
        user: "ArtLangdon",
        pwd: "ArtLangdon!@#",
        roles: [
            {
                role: "dbOwner",
                db: "file-center"
            }
        ]
    }
);
