package workplaceManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.model.Film;

import javax.jws.WebParam;

@Controller
public class FilmController {
    private static Film film;

    static {
        film = new Film();
        film.setTitle("Inception");
        film.setYear(2010);
        film.setGenre("sci-fi");
        film.setWatched(true);
    }

    @GetMapping("/")
    public ModelAndView allFilms() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("films");
        modelAndView.addObject("film", film);
        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView editPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editPage");
        return modelAndView;
    }
}
