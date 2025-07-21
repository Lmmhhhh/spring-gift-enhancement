package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.request.OptionRequest;
import gift.dto.response.OptionResponse;
import gift.exception.DuplicateOptionNameException;
import gift.exception.ProductNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OptionServiceImpl implements OptionService{
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionServiceImpl(OptionRepository optionRepository, ProductRepository productRepository){
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Page<OptionResponse> getOptions(Long productId, Pageable pageable) {
        if(!productRepository.existsById(productId)){
            throw new ProductNotFoundException(productId);
        }
        return optionRepository.findAllByProductId(productId, pageable)
                .map(OptionResponse::from);
    }
}
