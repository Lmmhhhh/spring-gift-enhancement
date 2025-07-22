package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.request.OptionRequest;
import gift.dto.response.OptionResponse;
import gift.exception.DuplicateOptionNameException;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionServiceImpl implements OptionService{
    private final ProductRepository productRepository;

    public OptionServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Page<OptionResponse> getOptions(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        List<Option> options = product.getOptions();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), options.size());

        List<OptionResponse> content = options.subList(start, end).stream()
                .map(OptionResponse::from)
                .toList();

        return new PageImpl<>(content, pageable, options.size());
    }
}
