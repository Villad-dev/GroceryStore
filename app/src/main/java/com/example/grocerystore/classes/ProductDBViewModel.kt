
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.classes.Product
import com.example.grocerystore.classes.ProductRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDBViewModel(private val app: Application) : AndroidViewModel(app) {

    private val productRepo: ProductRepository
    private val firebaseDatabase: FirebaseDatabase
    val products: StateFlow<HashMap<String, Product>>

    init{
        firebaseDatabase = FirebaseDatabase.getInstance()
        productRepo = ProductRepository(firebaseDatabase)
        products = productRepo.allProducts
        //val productDao = ProductDatabase.getDatabase(app).productDao()
        //productRepo = ProductRepository(productDao)
        //products = productRepo.allProducts
    }

    fun insertProduct(product: Product){
        viewModelScope.launch {
            productRepo.insert(product)
        }
    }

    fun updateProduct(product: Map.Entry<String, Product>){
        viewModelScope.launch {
            productRepo.update(product)
        }
    }

    fun updateProducts(products: Map<String, Product>){
        viewModelScope.launch {
            for(product in products){
                if(product.value.boughtQuantity == product.value.productQuantity){
                    deleteProduct(product.value.id)
                }
                else {
                    product.value.isBought = true
                    product.value.productQuantity -= product.value.boughtQuantity
                    product.value.boughtQuantity = 0
                    updateProduct(product)
                }
            }
        }
    }

    fun deleteProduct(productId: String){
        viewModelScope.launch {
            productRepo.delete(productId)
        }
    }
}