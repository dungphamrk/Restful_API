package ra.ss2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.ss2.entity.Showtime;
import ra.ss2.service.MovieService;
import ra.ss2.service.ScreenRoomService;
import ra.ss2.service.ShowtimeService;
import ra.ss2.service.TheaterService;

@Controller
@RequestMapping("/showtimes")
public class ShowtimeController {

    @Autowired
    private ShowtimeService ShowtimeService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private TheaterService theaterService;
    @Autowired
    private ScreenRoomService screenRoomService;

    @GetMapping
    public String showShowtimes(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Long cinemaId,
            @RequestParam(required = false) Long roomId,
            Model model) {

        String filteredDate = date;
        if (date != null && date.trim().isEmpty()) {
            filteredDate = null;
        }
        // Gọi phương thức lọc trong ShowtimeService
        model.addAttribute("showtimes", showtimeService.filterShowtimes(movieId, filteredDate, cinemaId, roomId));

        // Truyền dữ liệu dropdown
        model.addAttribute("movies", movieService.findAll());
        model.addAttribute("cinemas", theaterService.findAll());
        model.addAttribute("rooms", screenRoomService.findAll());

        return "showtime-list";
    }
    @GetMapping("/add")
    public String showAddShowtime(Model model) {
        model.addAttribute("showtime", new Showtime());
        model.addAttribute("movies", movieService.findAll());
        model.addAttribute("screenRooms", showtimeService.findAll());
        return "showtime-add";
    }

    @PostMapping("/add")
    public String addShowtime(@ModelAttribute("showtime") Showtime Showtime) {
        ShowtimeService.save(Showtime);
        return "redirect:/showtimes";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Showtime Showtime = ShowtimeService.findById(id);
        if (Showtime == null) {
            return "redirect:/showtimes";
        }
        model.addAttribute("movies", movieService.findAll());
        model.addAttribute("screenRooms", showtimeService.findAll());
        model.addAttribute("showtime", Showtime);
        return "showtime-edit";
    }

    @PostMapping("/edit/{id}")
    public String editShowtime(@PathVariable("id") Long id, @ModelAttribute("showtime") Showtime updatedShowtime) {
        updatedShowtime.setId(id);
        ShowtimeService.update(updatedShowtime);
        return "redirect:/showtimes";
    }
    @PostMapping("/delete/{id}")
    public String deleteShowtime(@PathVariable("id") Long id) {
        ShowtimeService.delete(id);
        return "redirect:/showtimes";
    }
}
