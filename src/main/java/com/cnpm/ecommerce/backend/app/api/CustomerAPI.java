package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.CustomerDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.User;
import com.cnpm.ecommerce.backend.app.service.IUserService;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import com.cnpm.ecommerce.backend.app.validationgroups.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin
public class CustomerAPI {

    @Autowired
    private IUserService customerService;

    @GetMapping("")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<Page<User>> findAll(@RequestParam(name = "q", required = false) String userName,
                                              @RequestParam(name = "enabled", required = false) Integer enabled,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int limit,
                                              @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<User> customerPage;

            if(userName == null) {
                customerPage = customerService.findAllPageAndSortCustomer(pagingSort);
            } else {
                customerPage = customerService.findByUserNameContainingCustomer(userName, pagingSort);
            }
            if(userName == null && enabled == null) {
                customerPage = customerService.findAllPageAndSortCustomer(pagingSort);
            } else {
                if(enabled == null){
                    customerPage = customerService.findByUserNameContainingCustomer(userName, pagingSort);
                } else if (userName == null) {
                    customerPage = customerService.findByEnabledCustomer(enabled, pagingSort);
                } else {
                    customerPage = customerService.findByUserNameContainingAndEnabledCustomer(userName, enabled, pagingSort);
                }

            }

            return new ResponseEntity<>(customerPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<CustomerDTO> findById(@PathVariable("id") Long theId){

        CustomerDTO theCustomer = customerService.findByIdCustomerDto(theId);
        return new ResponseEntity<>(theCustomer, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createCustomer(@Valid @RequestBody CustomerDTO theCustomerDto, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for create customer", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = customerService.createCustomer(theCustomerDto);
        return new ResponseEntity<MessageResponse>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<MessageResponse> updateCustomer(@PathVariable("id") Long theId,
                                                          @Validated(OnUpdate.class) @RequestBody CustomerDTO theCustomerDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for update customer", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = customerService.updateCustomer(theId, theCustomerDto);
        return new ResponseEntity<MessageResponse>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long theId){

        customerService.deleteCustomer(theId);
        return new ResponseEntity<>(new MessageResponse("Delete successfully!", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }


    @GetMapping("/count")
    public ResponseEntity<?> count(){
        return new ResponseEntity<>(customerService.countCustomer(), HttpStatus.OK);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> activeCustomer(@RequestParam(name = "username", required = true) String userName){

        MessageResponse messageResponse = customerService.activeCustomer(userName);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }
}
