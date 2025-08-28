name: Build and Push Docker Image to ECR

on:
  push:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest

    env:
      # Environment variables loaded from GitHub Secrets
      AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
      AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
      AWS_REGION: ${{ secrets.AWS_REGION }}
      AWS_ACCOUNT_ID: ${{ secrets.AWS_ACCOUNT_ID }}
      ECR_REPOSITORY: ${{ secrets.ECR_REPOSITORY }}

      # Application secrets
      APP_JWT_SECRET: ${{ secrets.APP_JWT_SECRET }}
      APP_JWT_EXPIRATION_MS: ${{ secrets.APP_JWT_EXPIRATION_MS }}

      # Database secrets
      SPRING_DATASOURCE_URL: ${{ secrets.SPRING_DATASOURCE_URL }}
      SPRING_DATASOURCE_USERNAME: ${{ secrets.SPRING_DATASOURCE_USERNAME }}
      SPRING_DATASOURCE_PASSWORD: ${{ secrets.SPRING_DATASOURCE_PASSWORD }}

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v4
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ${{ secrets.AWS_REGION }}

      - name: Login to Amazon ECR
        uses: aws-actions/amazon-ecr-login@v2

      - name: Build Docker image
        run: |
          IMAGE_TAG=${GITHUB_SHA::8}
          docker build \
            --build-arg SPRING_DATASOURCE_URL="${SPRING_DATASOURCE_URL}" \
            --build-arg SPRING_DATASOURCE_USERNAME="${SPRING_DATASOURCE_USERNAME}" \
            --build-arg SPRING_DATASOURCE_PASSWORD="${SPRING_DATASOURCE_PASSWORD}" \
            --build-arg APP_JWT_SECRET="${APP_JWT_SECRET}" \
            --build-arg APP_JWT_EXPIRATION_MS="${APP_JWT_EXPIRATION_MS}" \
            -t "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPOSITORY}:$IMAGE_TAG" .

      - name: Push Docker image to ECR
        run: |
          IMAGE_TAG=${GITHUB_SHA::8}
          docker push "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPOSITORY}:$IMAGE_TAG"
