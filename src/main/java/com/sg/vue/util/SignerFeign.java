package com.sg.vue.util;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface SignerFeign {
    @PostMapping("/member/signer/batchGenerateId")
    String batchGenerateId(@RequestBody Map map);

    @PostMapping("/member/signer/generateId")
    Long generateId(@RequestBody Map map);
}