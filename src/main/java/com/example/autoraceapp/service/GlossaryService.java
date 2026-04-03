package com.example.autoraceapp.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.autoraceapp.entity.GlossaryTerm;
import com.example.autoraceapp.repository.GlossaryTermRepository;

@Service
public class GlossaryService {

    private final GlossaryTermRepository glossaryTermRepository;

    public GlossaryService(GlossaryTermRepository glossaryTermRepository) {
        this.glossaryTermRepository = glossaryTermRepository;
    }

    @Transactional
    public List<GlossaryTerm> findTerms(String query) {
        initializeDefaults();

        String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        return glossaryTermRepository.findAll().stream()
                .filter(term -> matches(term, normalizedQuery))
                .sorted(Comparator
                        .comparing(GlossaryTerm::isChecked)
                        .thenComparing(GlossaryTerm::getTerm, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public GlossaryTerm createEmptyTerm() {
        return new GlossaryTerm();
    }

    @Transactional
    public void addTerm(GlossaryTerm glossaryTerm) {
        if (glossaryTerm == null || glossaryTerm.getTerm() == null || glossaryTerm.getTerm().isBlank()) {
            return;
        }
        glossaryTerm.setTerm(glossaryTerm.getTerm().trim());
        glossaryTerm.setChecked(Boolean.TRUE.equals(glossaryTerm.getChecked()));
        glossaryTermRepository.save(glossaryTerm);
    }

    @Transactional
    public void updateTerm(Long id, String term, String meaning, String memo, boolean checked) {
        glossaryTermRepository.findById(id).ifPresent(glossaryTerm -> {
            glossaryTerm.setTerm(term == null ? "" : term.trim());
            glossaryTerm.setMeaning(meaning == null ? "" : meaning.trim());
            glossaryTerm.setMemo(memo == null ? "" : memo.trim());
            glossaryTerm.setChecked(checked);
            glossaryTermRepository.save(glossaryTerm);
        });
    }

    private boolean matches(GlossaryTerm term, String query) {
        if (query.isBlank()) {
            return true;
        }
        return contains(term.getTerm(), query)
                || contains(term.getMeaning(), query)
                || contains(term.getMemo(), query);
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private void initializeDefaults() {
        if (glossaryTermRepository.count() > 0) {
            return;
        }

        List<GlossaryTerm> defaults = new ArrayList<>();
        defaults.add(createDefault("試走", "本走前に行う走行確認です。選手の動きや走路との相性を見る手がかりになります。"));
        defaults.add(createDefault("オッズ", "車券ごとの配当見込みです。人気の偏りを見るときに役立ちます。"));
        defaults.add(createDefault("走路状況", "乾走路か湿走路かなど、走りやすさに関わる状態です。"));
        defaults.add(createDefault("ハンデ", "選手ごとのスタート位置の差です。展開予想の基本になります。"));
        defaults.add(createDefault("スタート", "発走直後の出足です。序盤の位置取りに大きく影響します。"));
        glossaryTermRepository.saveAll(defaults);
    }

    private GlossaryTerm createDefault(String term, String meaning) {
        GlossaryTerm glossaryTerm = new GlossaryTerm();
        glossaryTerm.setTerm(term);
        glossaryTerm.setMeaning(meaning);
        glossaryTerm.setMemo("");
        glossaryTerm.setChecked(false);
        return glossaryTerm;
    }
}
