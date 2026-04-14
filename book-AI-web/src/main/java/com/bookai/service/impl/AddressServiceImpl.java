package com.bookai.service.impl;

import com.bookai.dto.AddressDTO;
import com.bookai.entity.UserAddress;
import com.bookai.mapper.UserAddressMapper;
import com.bookai.service.AddressService;
import com.bookai.vo.AddressVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<AddressVO> getUserAddresses(Long userId) {
        List<UserAddress> addresses = userAddressMapper.selectByUserId(userId);
        return addresses.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public AddressVO getAddressDetail(Long userId, Long addressId) {
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return null;
        }
        return convertToVO(address);
    }

    @Override
    @Transactional
    public boolean addAddress(Long userId, AddressDTO dto) {
        UserAddress address = new UserAddress();
        BeanUtils.copyProperties(dto, address);
        address.setUserId(userId);
        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());

        // 如果设置为默认地址，清除其他默认地址
        if (Integer.valueOf(1).equals(dto.getIsDefault())) {
            userAddressMapper.clearDefaultByUserId(userId);
        }

        return userAddressMapper.insert(address) > 0;
    }

    @Override
    @Transactional
    public boolean updateAddress(Long userId, AddressDTO dto) {
        UserAddress existing = userAddressMapper.selectById(dto.getId());
        if (existing == null || !existing.getUserId().equals(userId)) {
            return false;
        }

        UserAddress address = new UserAddress();
        BeanUtils.copyProperties(dto, address);
        address.setUserId(userId);
        address.setUpdateTime(LocalDateTime.now());

        // 如果设置为默认地址，清除其他默认地址
        if (Integer.valueOf(1).equals(dto.getIsDefault()) && !Integer.valueOf(1).equals(existing.getIsDefault())) {
            userAddressMapper.clearDefaultByUserId(userId);
        }

        return userAddressMapper.updateById(address) > 0;
    }

    @Override
    public boolean deleteAddress(Long userId, Long addressId) {
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return false;
        }
        return userAddressMapper.deleteById(addressId) > 0;
    }

    @Override
    @Transactional
    public boolean setDefaultAddress(Long userId, Long addressId) {
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return false;
        }

        userAddressMapper.clearDefaultByUserId(userId);

        address.setIsDefault(1);
        address.setUpdateTime(LocalDateTime.now());
        return userAddressMapper.updateById(address) > 0;
    }

    private AddressVO convertToVO(UserAddress address) {
        AddressVO vo = new AddressVO();
        BeanUtils.copyProperties(address, vo);
        return vo;
    }
}
