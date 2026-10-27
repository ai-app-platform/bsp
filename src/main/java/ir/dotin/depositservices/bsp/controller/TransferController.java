package ir.dotin.depositservices.bsp.controller;

import ir.dotin.depositservices.bsp.dto.TransferCreateRequest;
import ir.dotin.depositservices.bsp.dto.TransferResponse;
import ir.dotin.depositservices.bsp.dto.TransferUpdateRequest;
import ir.dotin.depositservices.bsp.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {
    private final TransferService service;

    public TransferController(TransferService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponse create(@Valid @RequestBody TransferCreateRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<TransferResponse> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public TransferResponse get(@PathVariable Long id) { return service.get(id); }

    @PutMapping("/{id}")
    public TransferResponse update(@PathVariable Long id, @Valid @RequestBody TransferUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
