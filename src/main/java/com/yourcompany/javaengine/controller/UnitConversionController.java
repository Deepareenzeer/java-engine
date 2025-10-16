package com.yourcompany.javaengine.controller;

import com.yourcompany.javaengine.model.UnitConversionRequest;
import com.yourcompany.javaengine.service.UnitConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller สำหรับจัดการคำขอแปลงหน่วย
 * Endpoint หลัก: /unit
 */
@RestController
@CrossOrigin(origins = "*") // อนุญาตการเข้าถึงจากทุก Domain เพื่อการทดสอบ
@RequestMapping("/unit")
public class UnitConversionController {

    private final UnitConversionService unitConversionService;

    // ใช้ Constructor Injection ซึ่งเป็นวิธีที่แนะนำ
    public UnitConversionController(UnitConversionService unitConversionService) {
        this.unitConversionService = unitConversionService;
    }

    /**
     * Endpoint สำหรับแปลงหน่วย
     * Path: POST /unit/convert
     * @param request JSON body ที่มี category, fromUnit, toUnit, และ value
     * @return ผลลัพธ์ในรูปแบบ JSON {"result": value}
     */
    @PostMapping("/convert")
    public ResponseEntity<Map<String, Double>> convertUnits(@RequestBody UnitConversionRequest request) {
        try {
            // เรียกใช้ Service เพื่อทำการแปลง
            double result = unitConversionService.convert(request);

            // สร้าง Map เพื่อส่งผลลัพธ์กลับในรูปแบบ {"result": 123.45}
            Map<String, Double> response = new HashMap<>();
            response.put("result", result);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // กรณีที่มีข้อผิดพลาดในการแปลง เช่น หน่วยไม่ถูกต้อง
            // คืนค่า 400 Bad Request และอาจจะใส่ข้อความ Error ใน Body ด้วย
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
             // กรณี Error อื่นๆ
            return ResponseEntity.internalServerError().build();
        }
    }
}
