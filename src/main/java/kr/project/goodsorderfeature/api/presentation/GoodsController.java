package kr.project.goodsorderfeature.api.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.project.goodsorderfeature.api.application.GoodsService;
import kr.project.goodsorderfeature.api.dto.GoodsRequest;
import kr.project.goodsorderfeature.api.dto.GoodsResponse;
import kr.project.goodsorderfeature.core.swagger.annotation.ParameterHidden;
import kr.project.goodsorderfeature.core.swagger.annotation.SwaggerApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@SwaggerApiResponses
@Tag(name = "상품")
@ExposesResourceFor(GoodsResponse.class)
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    private final GoodsService goodsService;
    private final EntityLinks entityLinks;

    @Operation(summary = "상품목록 조회")
    @GetMapping
    public Page<GoodsResponse> findAllByPageable(@ParameterHidden @PageableDefault Pageable pageable) {
        return goodsService.findAllByPageable(pageable);
    }

    @Operation(summary = "상품 조회")
    @GetMapping("/{goodsId}")
    public EntityModel<GoodsResponse> findByGoodsId(@PathVariable Long goodsId) {

        return EntityModel.of(goodsService.findByGoodsId(goodsId),
                    entityLinks.linkToItemResource(GoodsResponse.class, goodsId).withRel("update").withType(HttpMethod.PUT.name()),
                    entityLinks.linkToItemResource(GoodsResponse.class, goodsId).withRel("delete").withType(HttpMethod.DELETE.name())
                );
    }

    @Operation(summary = "상품 등록")
    @PostMapping
    public ResponseEntity<GoodsResponse> create(@Validated @RequestBody GoodsRequest goodsRequest) {
        final GoodsResponse goodsResponse = goodsService.create(goodsRequest);
        final URI location = entityLinks.linkForItemResource(GoodsResponse.class, goodsResponse.getGoodsId()).toUri();

        return ResponseEntity.created(location).body(goodsResponse);
    }

    @Operation(summary = "상품 수정")
    @PutMapping("/{goodsId}")
    public GoodsResponse update(@PathVariable Long goodsId, @Validated @RequestBody GoodsRequest goodsRequest) {

        return goodsService.update(goodsRequest.setGoodsId(goodsId));
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{goodsId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByGoodsId(@PathVariable Long goodsId) {
        goodsService.deleteByGoodsId(goodsId);
    }
}