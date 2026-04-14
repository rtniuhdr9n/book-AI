package com.bookai.controller.user;

import com.bookai.common.Result;
import com.bookai.dto.AddressDTO;
import com.bookai.service.AddressService;
import com.bookai.vo.AddressVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/list")
    public Result list(@RequestAttribute("userId") Long userId) {
        List<AddressVO> list = addressService.getUserAddresses(userId);
        return Result.success(list);
    }

    @GetMapping("/detail/{id}")
    public Result detail(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        AddressVO vo = addressService.getAddressDetail(userId, id);
        if (vo == null) {
            return Result.error("地址不存在");
        }
        return Result.success(vo);
    }

    @PostMapping("/add")
    public Result add(@RequestAttribute("userId") Long userId, @Valid @RequestBody AddressDTO dto) {
        boolean success = addressService.addAddress(userId, dto);
        if (success) {
            return Result.success();
        }
        return Result.error("添加失败");
    }

    @PutMapping("/update")
    public Result update(@RequestAttribute("userId") Long userId, @Valid @RequestBody AddressDTO dto) {
        boolean success = addressService.updateAddress(userId, dto);
        if (success) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        boolean success = addressService.deleteAddress(userId, id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除失败");
    }

    @PutMapping("/setDefault/{id}")
    public Result setDefault(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        boolean success = addressService.setDefaultAddress(userId, id);
        if (success) {
            return Result.success();
        }
        return Result.error("设置失败");
    }
}
