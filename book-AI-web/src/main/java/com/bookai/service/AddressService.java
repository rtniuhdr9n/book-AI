package com.bookai.service;

import com.bookai.dto.AddressDTO;
import com.bookai.vo.AddressVO;

import java.util.List;

public interface AddressService {

    List<AddressVO> getUserAddresses(Long userId);

    AddressVO getAddressDetail(Long userId, Long addressId);

    boolean addAddress(Long userId, AddressDTO dto);

    boolean updateAddress(Long userId, AddressDTO dto);

    boolean deleteAddress(Long userId, Long addressId);

    boolean setDefaultAddress(Long userId, Long addressId);
}
