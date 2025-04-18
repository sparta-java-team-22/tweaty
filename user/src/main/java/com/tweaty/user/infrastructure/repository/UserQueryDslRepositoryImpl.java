package com.tweaty.user.infrastructure.repository;

import static com.tweaty.user.domain.model.QOwner.*;
import static com.tweaty.user.domain.model.QUser.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tweaty.user.domain.model.ApprovalStatus;
import com.tweaty.user.domain.model.Owner;
import com.tweaty.user.domain.model.User;

import domain.Role;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryDslRepositoryImpl {

	private final JPAQueryFactory jpaQueryFactory;

	public Page<User> getUserList(String key, Pageable pageable, String sortBy, String order) {

		OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, order, "customer");

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(user.role.eq(Role.ROLE_CUSTOMER));

		if (key != null && !key.isBlank()) {
			builder.and(user.username.containsIgnoreCase(key));
		}

		List<User> userList = jpaQueryFactory
			.selectFrom(user)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(orderSpecifier)
			.fetch();

		long total = jpaQueryFactory
			.select(user.count())
			.from(user)
			.where(builder)
			.fetchOne();

		return new PageImpl<>(userList, pageable, total);
	}

	public Page<Owner> getOwnerList(String key, Pageable pageable, String sortBy, String order) {

		OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, order, "owner");

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(owner.user.role.eq(Role.ROLE_OWNER));

		if (key != null && !key.isBlank()) {
			builder.and(owner.user.username.containsIgnoreCase(key));
		}

		List<Owner> ownerList = jpaQueryFactory
			.selectFrom(owner)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(orderSpecifier)
			.fetch();

		long total = jpaQueryFactory
			.select(owner.count())
			.from(owner)
			.where(builder)
			.fetchOne();

		return new PageImpl<>(ownerList, pageable, total);
	}

	public Page<Owner> getPendingOwnersList(Pageable pageable, String sortBy, String order) {

		OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, order, "owner");

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(owner.user.role.eq(Role.ROLE_OWNER));
		builder.and(owner.approvalStatus.eq(ApprovalStatus.PENDING));

		List<Owner> ownerList = jpaQueryFactory
			.selectFrom(owner)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(orderSpecifier)
			.fetch();

		long total = jpaQueryFactory
			.select(owner.count())
			.from(owner)
			.where(builder)
			.fetchOne();

		return new PageImpl<>(ownerList, pageable, total);

	}

	private OrderSpecifier<?> getOrderSpecifier(String sortBy, String order, String role) {

		Order direction = order.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

		if ("owner".equals(role)) {
			if ("modifiedAt".equals(sortBy)) {
				return new OrderSpecifier<>(direction, owner.user.modifiedAt);
			} else {
				return new OrderSpecifier<>(direction, owner.user.createdAt);
			}
		} else {
			if ("modifiedAt".equals(sortBy)) {
				return new OrderSpecifier<>(direction, user.modifiedAt);
			} else {
				return new OrderSpecifier<>(direction, user.createdAt);
			}
		}
	}
}

