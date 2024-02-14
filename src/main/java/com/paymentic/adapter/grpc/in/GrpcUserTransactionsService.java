package com.paymentic.adapter.grpc.in;

import com.paymentic.adapter.in.grpc.UserMonthAverageRequest;
import com.paymentic.adapter.in.grpc.UserMonthAverageResponse;
import com.paymentic.adapter.in.grpc.UserTransactionsService;
import com.paymentic.domain.AverageTransactionsValue;
import com.paymentic.domain.application.UserTransactionService;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import java.time.LocalDate;

@GrpcService
public class GrpcUserTransactionsService implements UserTransactionsService {
  private final UserTransactionService userTransactionService;
  public GrpcUserTransactionsService(UserTransactionService userTransactionService) {
    this.userTransactionService = userTransactionService;
  }
  @Override
  public Uni<UserMonthAverageResponse> getUserMonthAverage(UserMonthAverageRequest request) {
    var at = LocalDate.parse(request.getMonth());
    AverageTransactionsValue average = this.userTransactionService.totalPerMonth(
        request.getDocument(), at);
    return Uni.createFrom().item(() -> UserMonthAverageResponse.newBuilder()
        .setDocument(average.getDocument())
        .setTotal(average.getAverage().toString())
        .setMonth(at.getMonth().toString())
        .build());
  }

}