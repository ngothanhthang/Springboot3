package vn.iostar.controllers.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import vn.iostar.entity.Category;
import vn.iostar.models.CategoryModel;
import vn.iostar.services.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
	@RequestMapping("")
	public String all(Model model){
		List<Category> list= categoryService.findAll();		
		model.addAttribute("list",list);
		
		return "admin/category/list";
	}
	
	@GetMapping("/add")
	public String add(Model model) {
		CategoryModel category=new CategoryModel();
		category.setIsEdit(false);
		model.addAttribute("category",category);
		
		return "admin/category/add";
	}
	

	@PostMapping("/save")
	public ModelAndView saveOrUpdate(ModelMap model,
	@Valid @ModelAttribute("category") CategoryModel cateMdoel, BindingResult result) {
	if(result.hasErrors()) {
	return new ModelAndView("admin/category/add");
	}
	Category entity = new Category();
	//copy từ Model sang Entity
	BeanUtils.copyProperties(cateMdoel, entity);
	//goi ham save trong service
	categoryService.save(entity);
	//đưa thông báo về cho bien message
	String message= "";
	if(cateMdoel.getIsEdit() == true) {
	message="Category is Edited !!!!!!!! ";
	
	}else {
	message="Category is saved! !!!!!!! ";
	}
	model.addAttribute("message",message);
	//redirect ve URL controller
	return new ModelAndView("forward:/admin/categories",model);
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView edit(ModelMap model,@PathVariable("id") Long categoryId) {
	Optional<Category> optCategory = categoryService.findById(categoryId);
	CategoryModel cateModel = new CategoryModel();
	//kiểm tra sự tồn tại của category
	if(optCategory.isPresent()) {
	Category entity = optCategory.get();
	//copy tu entity sang cateModel
	BeanUtils.copyProperties(entity, cateModel);
	cateModel.setIsEdit(true);
	//đầy dữ liệu ra view
	model.addAttribute("category",cateModel);

	return new ModelAndView("admin/category/add",model);
	}

	model.addAttribute("message","Category is not existed !!!! ");
	return new ModelAndView("forward:/admin/categories",model);

	}
	
	@GetMapping("delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long categoryId) {
	categoryService.deleteById(categoryId);
	model.addAttribute("message","Category is deleted !!!! ");
	return new ModelAndView("forward:/admin/categories",model);
	}
}


