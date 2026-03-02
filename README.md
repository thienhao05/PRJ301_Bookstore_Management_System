PRJ301_Book_Management_System

- Database Diagram
  ![alt text](image-4.png)

```markdown
BookstoreProject/
├── src/
│ ├── java/
│ │ ├── controller/
│ │ │ ├── admin/ # Dành riêng cho Admin/Manager quản lý CRUD
│ │ │ │ ├── AdminBookController.java
│ │ │ │ ├── AdminCategoryController.java
│ │ │ │ ├── AdminDashboardController.java
│ │ │ │ ├── AdminDiscountController.java
│ │ │ │ ├── AdminNewsController.java
│ │ │ │ ├── AdminOrderController.java
│ │ │ │ ├── AdminPublisherController.java
│ │ │ │ ├── AdminRoleController.java
│ │ │ │ ├── AdminShippingController.java
│ │ │ │ ├── AdminStaffController.java
│ │ │ │ └── AdminUserController.java
│ │ │ │
│ │ │ ├── AuthController.java # Xử lý Login, Register, Logout
│ │ │ ├── BookController.java # Hiển thị sách cho User (Search, Filter, Paging)
│ │ │ ├── CartController.java # Quản lý giỏ hàng (Add, Update, Remove)
│ │ │ ├── CheckoutController.java # Xử lý thanh toán, áp mã giảm giá, chọn vận chuyển
│ │ │ ├── HomeController.java # Load trang chủ (Sách mới, Sách hot, Tin tức)
│ │ │ ├── MainController.java # Front Controller (Điều hướng mọi request)
│ │ │ ├── ProfileController.java # Quản lý thông tin User, Địa chỉ, Đổi mật khẩu
│ │ │ └── UserActionController.java # Gánh các tính năng phụ: Wishlist, Review, Notifications
│ │ │
│ │ ├── dao/ # 19 class DAO + 1 Interface
│ │ │ ├── ICRUD.java # <--- Tuyệt chiêu ăn điểm (Generic Interface)
│ │ │ ├── AddressDAO.java
│ │ │ ├── BookDAO.java
│ │ │ ├── CartDAO.java
│ │ │ ├── CartItemDAO.java
│ │ │ ├── CategoryDAO.java
│ │ │ ├── DiscountDAO.java
│ │ │ ├── NewsDAO.java
│ │ │ ├── NotificationDAO.java
│ │ │ ├── OrderDAO.java
│ │ │ ├── OrderDetailDAO.java
│ │ │ ├── PaymentDAO.java
│ │ │ ├── PublisherDAO.java
│ │ │ ├── ReviewDAO.java
│ │ │ ├── RoleDAO.java
│ │ │ ├── ShiftDAO.java
│ │ │ ├── ShippingProviderDAO.java
│ │ │ ├── StaffDAO.java
│ │ │ ├── UserDAO.java
│ │ │ └── WishlistDAO.java
│ │ │
│ │ ├── dto/ # 19 class DTO ánh xạ 1-1 với 19 bảng DB
│ │ │ ├── AddressDTO.java
│ │ │ ├── BookDTO.java
│ │ │ ├── CartDTO.java
│ │ │ ├── CartItemDTO.java
│ │ │ ├── CategoryDTO.java
│ │ │ ├── DiscountDTO.java
│ │ │ ├── NewsDTO.java
│ │ │ ├── NotificationDTO.java
│ │ │ ├── OrderDTO.java
│ │ │ ├── OrderDetailDTO.java
│ │ │ ├── PaymentDTO.java
│ │ │ ├── PublisherDTO.java
│ │ │ ├── ReviewDTO.java
│ │ │ ├── RoleDTO.java
│ │ │ ├── ShiftDTO.java
│ │ │ ├── ShippingProviderDTO.java
│ │ │ ├── StaffDTO.java
│ │ │ ├── UserDTO.java
│ │ │ └── WishlistDTO.java
│ │ │
│ │ └── utils/
│ │ ├── Constants.java # Chứa các hằng số (Status: Active, Pending...)
│ │ ├── DbUtils.java # Kết nối CSDL SQL Server (Singleton Pattern)
│ │ └── PasswordUtil.java # Hàm băm mật khẩu bảo mật
│ │
│ └── web/ # Quy hoạch lại giao diện gọn gàng
│ ├── META-INF/
│ ├── WEB-INF/
│ │ └── web.xml
│ ├── assets/
│ │ ├── css/
│ │ ├── images/
│ │ └── js/
│ ├── views/
│ │ ├── admin/ # Chứa toàn bộ JSP của Admin
│ │ │ ├── dashboard.jsp
│ │ │ ├── manage-books.jsp
│ │ │ └── ...
│ │ ├── components/ # Các phần dùng chung (Dùng <jsp:include>)
│ │ │ ├── admin-sidebar.jsp
│ │ │ ├── footer.jsp
│ │ │ └── header.jsp
│ │ └── user/ # Chứa toàn bộ JSP của Khách hàng
│ │ ├── cart.jsp
│ │ ├── checkout.jsp
│ │ ├── home.jsp
│ │ ├── login.jsp
│ │ ├── profile.jsp
│ │ └── shop.jsp
│ └── index.jsp # Chỉ dùng để forward tới MainController
```
