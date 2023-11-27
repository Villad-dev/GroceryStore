import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.classes.Product
import com.example.grocerystore.classes.ProductDatabase
import com.example.grocerystore.classes.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductDBViewModel(private val app: Application) : AndroidViewModel(app) {

    private val productRepo: ProductRepository
    val products: Flow<List<Product>>


    init{
        val productDao = ProductDatabase.getDatabase(app).productDao()
        productRepo = ProductRepository(productDao)
        products = productRepo.allProducts
    }

    fun insertProduct(product: Product){
        viewModelScope.launch {
            productRepo.insert(product)
        }
    }

    fun updateProduct(product: Product){
        viewModelScope.launch {
            productRepo.update(product)
        }
    }

    fun updateProducts(products: List<Product>){
        viewModelScope.launch {
            for(product in products){
                if(product.boughtQuantity == product.productQuantity){
                    deleteProduct(product)
                }
                else {
                    product.isBought = true
                    product.productQuantity -= product.boughtQuantity
                    product.boughtQuantity = 0
                    updateProduct(product)
                }
            }
        }
    }

    fun deleteProduct(product: Product){
        viewModelScope.launch {
            productRepo.delete(product)
        }
    }
}