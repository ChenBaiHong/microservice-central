package com.baimicro.central.platform.system.controller;

import com.baimicro.central.platform.system.service.NgAlainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/platform/ng-alain")
public class NgAlainController {
    @Autowired
    private NgAlainService ngAlainService;



    @RequestMapping(value = "/getByTable/{table}/{key}/{value}", method = RequestMethod.GET)
    public Object getDictItemsByTable(@PathVariable String table, @PathVariable String key, @PathVariable String value) {
        return this.ngAlainService.getByTable(table, key, value);
    }
}
