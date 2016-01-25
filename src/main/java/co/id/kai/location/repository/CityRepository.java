/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.kai.location.repository;

import co.id.kai.lib.location.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 *
 * @author Irpan Budiana
 */
public interface CityRepository extends PagingAndSortingRepository<City, String> {
    Page<City> findByStatusAndNameContainingIgnoreCase(String status, String name, Pageable page);
    Page<City> findByStatusAndId(String status, String id, Pageable page);
}