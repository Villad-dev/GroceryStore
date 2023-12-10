import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.classes.Product
import com.example.grocerystore.classes.ProductRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDBViewModel(app: Application) : AndroidViewModel(app) {

    private val productRepo: ProductRepository
    private val firebaseDatabase: FirebaseDatabase
    var products: StateFlow<HashMap<String, Product>>

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
        productRepo = ProductRepository(firebaseDatabase)
        products = productRepo.allProducts
    }

    fun updateList(){
        products = productRepo.allProducts
    }

    fun switchList(newList: String) {
        productRepo.switchList(newList)
    }

    fun insertProduct(product: Product) {
        viewModelScope.launch {
            productRepo.insert(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            productRepo.update(product)
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            productRepo.delete(productId)
        }
    }


    fun updateProducts(products: List<Pair<String, Product>>) {
        viewModelScope.launch {
            for (product in products) {
                if (product.second.boughtQuantity == product.second.productQuantity) {
                    deleteProduct(product.second.id)
                } else {
                    product.second.bought = true
                    product.second.productQuantity -= product.second.boughtQuantity
                    product.second.boughtQuantity = 0
                    updateProduct(product.second)
                }
            }
        }
    }
}
