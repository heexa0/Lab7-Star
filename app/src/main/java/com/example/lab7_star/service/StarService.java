package com.example.lab7.service;

import com.example.lab7.beans.Star;
import com.example.lab7.dao.IDao;

import java.util.ArrayList;
import java.util.List;

public class StarService implements IDao<Star> {

    private List<Star> stars;
    private static StarService instance;

    private StarService() {
        stars = new ArrayList<>();
        seed();
    }

    public static StarService getInstance() {
        if (instance == null) {
            instance = new StarService();
        }
        return instance;
    }

    private void seed() {
        stars.add(new Star("Kate Bosworth",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e6/Kate_Bosworth_Deauville_2011.jpg/500px-Kate_Bosworth_Deauville_2011.jpg",
                3.0f));
        stars.add(new Star("George Clooney",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/George_Clooney_Jay_Kelly-19_%28cropped%29.jpg/500px-George_Clooney_Jay_Kelly-19_%28cropped%29.jpg",
                3.5f));
        stars.add(new Star("Michelle Rodriguez",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fd/Michelle_Rodriguez_Cannes_2018_cropped.jpg/500px-Michelle_Rodriguez_Cannes_2018_cropped.jpg",
                5.0f));
        stars.add(new Star("Brad Pitt",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Brad_Pitt-69858.jpg/500px-Brad_Pitt-69858.jpg",
                4.5f));
        stars.add(new Star("Angelina Jolie",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/Angelina_Jolie-643531_%28cropped%29.jpg/500px-Angelina_Jolie-643531_%28cropped%29.jpg",
                4.8f));
        stars.add(new Star("Scarlett Johansson",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Scarlett_Johansson-8588.jpg/500px-Scarlett_Johansson-8588.jpg",
                4.7f));
    }

    @Override
    public boolean create(Star o) {
        return stars.add(o);
    }

    @Override
    public boolean update(Star o) {
        for (Star s : stars) {
            if (s.getId() == o.getId()) {
                s.setName(o.getName());
                s.setImg(o.getImg());
                s.setStar(o.getStar());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Star o) {
        return stars.remove(o);
    }

    @Override
    public Star findById(int id) {
        for (Star s : stars) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    @Override
    public List<Star> findAll() {
        return stars;
    }
}