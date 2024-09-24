package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/24 15:21<br/>
 */
public interface AddressBookService {
    
    /**
     * 查询当前登录用户的所有地址信息
     *
     * @param addressBook 通讯录
     * @return {@link List }<{@link AddressBook }>
     */
    List<AddressBook> list(AddressBook addressBook);
    
    /**
     * 新增地址
     *
     * @param addressBook 通讯录
     */
    void save(AddressBook addressBook);
    
    /**
     * 按 ID 获取地址
     *
     * @param id 通讯录id
     * @return {@link AddressBook }
     */
    AddressBook getById(Long id);
    
    /**
     * 根据id修改地址
     *
     * @param addressBook 通讯录
     */
    void update(AddressBook addressBook);
    
    /**
     * 设置默认地址
     *
     * @param addressBook 通讯录
     */
    void setDefault(AddressBook addressBook);
    
    /**
     * 根据id删除地址
     *
     * @param id 通讯录id
     */
    void deleteById(Long id);
    
}