/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.kai.location.repository;

import co.id.kai.lib.location.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 *
 * @author Irpan Budiana
 */
public interface ProvinceRepository extends PagingAndSortingRepository<Province, String> {
    Page<Province> findByStatusAndNameContainingIgnoreCase( String status, String name, Pageable page);
    Page<Province> findByStatusAndId(String status, String id, Pageable page);
}