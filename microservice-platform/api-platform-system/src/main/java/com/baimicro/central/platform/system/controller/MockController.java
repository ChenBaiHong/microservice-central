package com.baimicro.central.platform.system.controller;


import com.baimicro.central.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class MockController {

    private final String JSON_PATH = "classpath:com/baihealth/cloud/platform/system/rpcservice/json";

    /**
     * 通用json访问接口
     * 格式： http://localhost:8080/jeecg-boot/api/json/{filename}
     *
     * @param filename
     * @return
     */
    @RequestMapping(value = "/json/{filename}", method = RequestMethod.GET)
    public String getJsonData(@PathVariable String filename) {
        String jsonpath = "classpath:com/baimicro/central/platform/system/controller/json" + filename + ".json";
        return readJson(jsonpath);
    }

    @GetMapping(value = "/asynTreeList")
    public String asynTreeList(String id) {
        return readJson(JSON_PATH + "/asyn_tree_list_" + id + ".json");
    }

    @GetMapping(value = "/user")
    public String user() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonuser.json");
    }

    /**
     * 老的登录获取用户信息接口
     *
     * @return
     */
    @GetMapping(value = "/user/info")
    public String userInfo() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonuser_info.json");
    }

    @GetMapping(value = "/role")
    public String role() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonrole.json");
    }

    @GetMapping(value = "/service")
    public String service() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonrpcservice.json");
    }

    @GetMapping(value = "/permission")
    public String permission() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonpermission.json");
    }

    @GetMapping(value = "/permission/no-pager")
    public String permission_no_page() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonpermission_no_page.json");
    }

    /**
     * 省市县
     */
    @GetMapping(value = "/area")
    public String area() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonarea.json");
    }

    /**
     * 测试报表数据
     */
    @GetMapping(value = "/report/getYearCountInfo")
    public String getYearCountInfo() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsongetCntrNoCountInfo.json");
    }

    @GetMapping(value = "/report/getMonthCountInfo")
    public String getMonthCountInfo() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsongetCntrNoCountInfo.json");
    }

    @GetMapping(value = "/report/getCntrNoCountInfo")
    public String getCntrNoCountInfo() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsongetCntrNoCountInfo.json");
    }

    @GetMapping(value = "/report/getCabinetCountInfo")
    public String getCabinetCountInfo() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsongetCntrNoCountInfo.json");
    }

    /**
     * 实时磁盘监控
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/queryDiskInfo")
    public Result<List<Map<String, Object>>> queryDiskInfo(HttpServletRequest request, HttpServletResponse response) {
        Result<List<Map<String, Object>>> res = new Result<>();
        try {
            // 当前文件系统类
            FileSystemView fsv = FileSystemView.getFileSystemView();
            // 列出所有windows 磁盘
            File[] fs = File.listRoots();
            log.info("查询磁盘信息:" + fs.length + "个");
            List<Map<String, Object>> list = new ArrayList<>();

            for (int i = 0; i < fs.length; i++) {
                if (fs[i].getTotalSpace() == 0) {
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("name", fsv.getSystemDisplayName(fs[i]));
                map.put("max", fs[i].getTotalSpace());
                map.put("rest", fs[i].getFreeSpace());
                map.put("restPPT", fs[i].getFreeSpace() * 100 / fs[i].getTotalSpace());
                list.add(map);
                log.info(map.toString());
            }
            res.setResult(list);
            res.setMessage("查询成功");
        } catch (Exception e) {
            res = Result.failed("查询失败" + e.getMessage());
        }
        return res;
    }

    //-------------------------------------------------------------------------------------------

    /**
     * 工作台首页的数据
     *
     * @return
     */
    @GetMapping(value = "/list/search/projects")
    public String projects() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonworkplace_projects.json");
    }

    @GetMapping(value = "/workplace/activity")
    public String activity() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonworkplace_activity.json");
    }

    @GetMapping(value = "/workplace/teams")
    public String teams() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonworkplace_teams.json");
    }

    @GetMapping(value = "/workplace/radar")
    public String radar() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonworkplace_radar.json");
    }

    @GetMapping(value = "/task/process")
    public String taskProcess() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsontask_process.json");
    }
    //-------------------------------------------------------------------------------------------

    //author:lvdandan-----date：20190315---for:添加数据日志json----
    public String sysDataLogJson() {
        return readJson("classpath:com/baimicro/central/platform/system/controller/jsonsysdatalog.json");
    }
    //author:lvdandan-----date：20190315---for:添加数据日志json----

    /**
     * 读取json格式文件
     *
     * @param jsonSrc
     * @return
     */
    private String readJson(String jsonSrc) {
        String json = "";
        try {
            //File jsonFile = ResourceUtils.getFile(jsonSrc);
            //json = FileUtils.re.readFileToString(jsonFile);
            //换个写法，解决springboot读取jar包中文件的问题
            InputStream stream = getClass().getClassLoader().getResourceAsStream(jsonSrc.replace("classpath:", ""));
            json = IOUtils.toString(stream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return json;
    }

}
