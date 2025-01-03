import React from "react";
import styled from "styled-components";

const ProgressContainer = styled.div`
  width: 100%;
  padding: 5px 0;
`;

const Label = styled.div`
  font-size: 0.9rem;
  color: #c4c4c4;
  display: flex;
  justify-content: space-between;
`;

const Bar = styled.div`
  background-color: #2b2d42;
  border-radius: 5px;
  overflow: hidden;
  height: 8px;
  margin-top: 5px;
`;

const Filled = styled.div`
  background-color: #3e98c7;
  width: ${(props) => props.width || 0}%;
  height: 100%;
  transition: width 0.5s ease-out; /* Smooth transition for width change */
`;

function ProgressBar({ label, value, max, unit }) {
  const percentage = (value / max) * 100;

  return (
    <ProgressContainer>
      <Label>
        <span>{label}</span>
        <span>{value} {unit}</span>
      </Label>
      <Bar>
        <Filled width={percentage} />
      </Bar>
    </ProgressContainer>
  );
}

export default ProgressBar;
