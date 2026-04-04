import styled from "styled-components";

export const CardContainer = styled.div`
  height: 140px;
  padding: 24px;

  border-radius: 18px;

  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(10px);

  border: 1px solid rgba(255, 255, 255, 0.1);

  display: flex;
  flex-direction: column;
  justify-content: center;
`;

export const CardTitle = styled.h2`
  font-size: 16px;
  margin-bottom: 12px;
  opacity: 0.8;
`;

export const CardValue = styled.span`
  font-size: 40px;
  font-weight: 700;
`;