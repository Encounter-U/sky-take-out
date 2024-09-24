package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/24 15:18<br/>
 */
@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C端-地址簿接口")
public class AddressBookController {
    
    @Autowired
    private AddressBookService addressBookService;
    
    /**
     * 查询当前登录用户的所有地址信息
     *
     * @return {@link Result }<{@link List }<{@link AddressBook }>>
     */
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }
    
    /**
     * 新增地址
     *
     * @param addressBook 通讯录
     * @return {@link Result }
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }
    
    /**
     * 按 ID 获取地址
     *
     * @param id 地址簿id
     * @return {@link Result }<{@link AddressBook }>
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }
    
    /**
     * 根据id修改地址
     *
     * @param addressBook 通讯录
     * @return {@link Result }
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }
    
    /**
     * 设置默认地址
     *
     * @param addressBook 通讯录
     * @return {@link Result }
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
    
    /**
     * 根据id删除地址  前端请求中多了个反斜杠
     *
     * @param id 通讯录id
     * @return {@link Result }
     */
    @DeleteMapping("/")
    @ApiOperation("根据id删除地址")
    public Result deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }
    
    /**
     * 查询默认地址
     *
     * @return {@link Result }<{@link AddressBook }>
     */
    @GetMapping("default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() {
        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        
        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }
        
        return Result.error("没有查询到默认地址");
    }
    
}
