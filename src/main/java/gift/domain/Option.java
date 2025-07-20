package gift.domain;

import jakarta.persistence.*;

import java.security.PublicKey;

@Entity
@Table(name = "product_option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="option_name", length = 50, nullable = false)
    private String name;

    @Column(name ="quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false,
        foreignKey = @ForeignKey(name = "fk_option_product_id_ref_product_id"))
    private Product product;

    protected Option(

    ){}

    public Option(String name, int quantity, Product product){
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void assignTo(Product product){
        this.product = product;
    }

    public Long getId() {return id;}

    public String getName() {return name;}

    public int getQuantity() {return quantity;}

    public Product getProduct() {return product;}
}
